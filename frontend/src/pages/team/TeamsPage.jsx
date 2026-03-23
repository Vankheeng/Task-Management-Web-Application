import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import teamApi from '../../api/teamApi'
import Layout from '../../components/common/Layout'
import Modal from '../../components/common/Modal'
import ConfirmDialog from '../../components/common/ConfirmDialog'
import { HiPlus, HiPencil, HiTrash, HiCheck, HiX, HiOutlineUsers } from 'react-icons/hi'
import { useToast } from '../../components/common/Toast'
import dayjs from 'dayjs'

export default function TeamsPage() {
  const toast = useToast()
  const [teams, setTeams] = useState([])
  const [editingId, setEditingId] = useState(null)
  const [editName, setEditName] = useState('')
  const [showCreate, setShowCreate] = useState(false)
  const [newName, setNewName] = useState('')
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [loading, setLoading] = useState(true)
  const navigate = useNavigate()

  const fetchTeams = async () => {
    try {
      const res = await teamApi.getMyTeams()
      setTeams(res.result || [])
    } finally { setLoading(false) }
  }

  useEffect(() => { fetchTeams() }, [])

  const handleCreate = async () => {
    if (!newName.trim()) return
    try {
      await teamApi.createTeam({ name: newName.trim() })
      setNewName('')
      setShowCreate(false)
      fetchTeams()
      toast.success('Team created')
    } catch (e) {
      toast.error(e?.message || 'Failed to create team')
    }
  }

  const handleUpdate = async (id) => {
    if (!editName.trim()) return
    await teamApi.updateTeam(id, { name: editName.trim() })
    setEditingId(null)
    fetchTeams()
  }

  const handleDelete = async (id) => {
    const team = teams.find(t => t.id === id)
    if (team?.myRole !== 'ADMIN') {
      toast.error('Only admins can delete a team')
      setDeleteTarget(null)
      return
    }
    try {
      await teamApi.deleteTeam(id)
      fetchTeams()
      setDeleteTarget(null)
      toast.success('Team deleted')
    } catch (e) {
      toast.error(e?.message || 'Failed to delete team')
      setDeleteTarget(null)
    }
  }

  return (
    <Layout>
      <div className="fade-in">
        <h1 className="font-display font-bold text-3xl text-primary mb-6">Teams</h1>

        <div className="card overflow-hidden">
          {/* Header */}
          <div className="grid grid-cols-12 px-4 py-3 bg-surface border-b border-border text-xs font-semibold text-slate-500 uppercase tracking-wide">
            <div className="col-span-5">Name</div>
            <div className="col-span-3">Created at</div>
            <div className="col-span-2">Members</div>
            <div className="col-span-2 text-right">Actions</div>
          </div>

          {loading ? (
            <div className="p-8 text-center text-slate-400 text-sm">Loading...</div>
          ) : teams.length === 0 ? (
            <div className="p-8 text-center text-slate-400 text-sm">No teams yet</div>
          ) : (
            teams.map(team => (
              <div
                key={team.id}
                className="grid grid-cols-12 items-center px-4 py-3 border-b border-border last:border-0 hover:bg-surface transition-colors"
              >
                {/* Name */}
                <div className="col-span-5">
                  {editingId === team.id ? (
                    <input
                      className="input py-1 text-sm"
                      value={editName}
                      onChange={e => setEditName(e.target.value)}
                      onKeyDown={e => e.key === 'Enter' && handleUpdate(team.id)}
                      autoFocus
                      onClick={e => e.stopPropagation()}
                    />
                  ) : (
                    <button
                      onClick={() => { localStorage.setItem('selectedTeamId', team.id); localStorage.setItem('selectedTeamName', team.name); localStorage.setItem('selectedTeamRole', team.myRole || ''); navigate(`/teams/${team.id}`) }}
                      className="text-primary font-medium text-sm hover:underline text-left"
                    >
                      {team.name}
                    </button>
                  )}
                </div>

                {/* Date */}
                <div className="col-span-3 text-sm text-slate-500">
                  {dayjs(team.createdAt).format('D/M/YYYY')}
                </div>

                {/* Members */}
                <div className="col-span-2 flex items-center gap-1 text-sm text-slate-500">
                  <HiOutlineUsers className="text-slate-400" />
                  {team.memberCount ?? 0}
                </div>

                {/* Actions */}
                <div className="col-span-2 flex justify-end gap-2">
                  {editingId === team.id ? (
                    <>
                      <button onClick={() => handleUpdate(team.id)} className="text-primary hover:text-primary-dark">
                        <HiCheck className="text-lg" />
                      </button>
                      <button onClick={() => setEditingId(null)} className="text-slate-400 hover:text-slate-600">
                        <HiX className="text-lg" />
                      </button>
                    </>
                  ) : (
                    <>
                      <button
                        onClick={(e) => { e.stopPropagation(); setEditingId(team.id); setEditName(team.name) }}
                        className="text-slate-400 hover:text-primary transition-colors"
                      >
                        <HiPencil className="text-lg" />
                      </button>
                      <button
                        onClick={(e) => { e.stopPropagation(); setDeleteTarget(team) }}
                        className="text-slate-400 hover:text-red-500 transition-colors"
                      >
                        <HiTrash className="text-lg" />
                      </button>
                    </>
                  )}
                </div>
              </div>
            ))
          )}

          {/* Add row */}
          <button
            onClick={() => setShowCreate(true)}
            className="w-full py-3 text-sm text-primary hover:bg-surface transition-colors flex items-center justify-center gap-1"
          >
            <HiPlus /> Add a team
          </button>
        </div>
      </div>

      {/* Create Modal */}
      <Modal open={showCreate} onClose={() => setShowCreate(false)} title="Create Team" size="sm">
        <div className="space-y-4">
          <input
            className="input"
            placeholder="Team name"
            value={newName}
            onChange={e => setNewName(e.target.value)}
            onKeyDown={e => e.key === 'Enter' && handleCreate()}
            autoFocus
          />
          <div className="flex gap-2 justify-end">
            <button onClick={() => setShowCreate(false)} className="btn-secondary">Cancel</button>
            <button onClick={handleCreate} className="btn-primary">Create</button>
          </div>
        </div>
      </Modal>

      {/* Delete Confirm */}
      <ConfirmDialog
        open={!!deleteTarget}
        onClose={() => setDeleteTarget(null)}
        onConfirm={() => handleDelete(deleteTarget?.id)}
        title="Delete Team"
        message={`Are you sure you want to delete "${deleteTarget?.name}"? This will also delete all projects, tasks and members.`}
      />
    </Layout>
  )
}