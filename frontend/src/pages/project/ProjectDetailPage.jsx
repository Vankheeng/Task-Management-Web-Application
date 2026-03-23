import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import projectApi from '../../api/projectApi'
import taskListApi from '../../api/taskListApi'
import statusApi from '../../api/statusApi'
import teamMemberApi from '../../api/teamMemberApi'
import Layout from '../../components/common/Layout'
import Modal from '../../components/common/Modal'
import ConfirmDialog from '../../components/common/ConfirmDialog'
import { useToast } from '../../components/common/Toast'
import { useAuth } from '../../context/AuthContext'
import { HiPlus, HiPencil, HiTrash, HiCheck, HiX, HiLockClosed } from 'react-icons/hi'
import dayjs from 'dayjs'

export default function ProjectDetailPage() {
  const { projectId } = useParams()
  const navigate = useNavigate()
  const toast = useToast()
  const { user } = useAuth()

  const [project, setProject] = useState(null)
  const [taskLists, setTaskLists] = useState([])
  const [statuses, setStatuses] = useState([])
  const [myRole, setMyRole] = useState(null)
  const [view, setView] = useState('tasklists')

  const [editingId, setEditingId] = useState(null)
  const [editName, setEditName] = useState('')
  const [showCreateTL, setShowCreateTL] = useState(false)
  const [newTLName, setNewTLName] = useState('')
  const [deleteTarget, setDeleteTarget] = useState(null)

  const [editingStatusId, setEditingStatusId] = useState(null)
  const [editStatusName, setEditStatusName] = useState('')
  const [showCreateStatus, setShowCreateStatus] = useState(false)
  const [newStatusName, setNewStatusName] = useState('')
  const [deleteStatusTarget, setDeleteStatusTarget] = useState(null)

  const [editingProject, setEditingProject] = useState(false)
  const [projectForm, setProjectForm] = useState({ name: '', description: '' })

  const teamId = localStorage.getItem('selectedTeamId') || ''
  const isAdmin = myRole === 'ADMIN'

  const fetchRole = async () => {
    try {
      const res = await teamMemberApi.getByTeamId(teamId)
      const me = (res.result || []).find(m => m.user?.id === user?.id)
      setMyRole(me?.role || 'MEMBER')
    } catch {}
  }

  const fetchProject = async () => {
    try {
      const res = await projectApi.getByTeamId(teamId)
      const found = (res.result || []).find(p => p.id === projectId)
      if (found) {
        setProject(found)
        setProjectForm({ name: found.name, description: found.description || '' })
      }
    } catch {}
  }

  const fetchTaskLists = async () => {
    try {
      const res = await taskListApi.getByProjectId(projectId)
      setTaskLists(res.result || [])
    } catch {}
  }

  const fetchStatuses = async () => {
    try {
      const res = await statusApi.getByProjectId(projectId)
      setStatuses(res.result || [])
    } catch {}
  }

  useEffect(() => {
    fetchRole()
    fetchProject()
    fetchTaskLists()
    fetchStatuses()
  }, [projectId])

  const requireAdmin = (fn) => {
    if (!isAdmin) { toast.error('Only admins can perform this action'); return }
    fn()
  }

  const handleCreateTL = async () => {
    if (!newTLName.trim()) return
    try {
      await taskListApi.create({ name: newTLName, projectId })
      setNewTLName(''); setShowCreateTL(false)
      fetchTaskLists()
      toast.success('Task list created')
    } catch (e) { toast.error(e?.message || 'Failed to create task list') }
  }

  const handleUpdateTL = async (id) => {
    try {
      await taskListApi.update(id, { name: editName, projectId })
      setEditingId(null)
      fetchTaskLists()
      toast.success('Task list updated')
    } catch (e) { toast.error(e?.message || 'Failed to update task list') }
  }

  const handleDeleteTL = async (id) => {
    try {
      await taskListApi.delete(id)
      fetchTaskLists()
      toast.success('Task list deleted')
    } catch (e) { toast.error(e?.message || 'Failed to delete task list') }
  }

  const handleCreateStatus = async () => {
    if (!newStatusName.trim()) return
    try {
      await statusApi.create({ status: newStatusName, projectId })
      setNewStatusName(''); setShowCreateStatus(false)
      fetchStatuses()
      toast.success('Status created')
    } catch (e) { toast.error(e?.message || 'Failed to create status') }
  }

  const handleUpdateStatus = async (id) => {
    try {
      await statusApi.update(id, { status: editStatusName, projectId })
      setEditingStatusId(null)
      fetchStatuses()
      toast.success('Status updated')
    } catch (e) { toast.error(e?.message || 'Failed to update status') }
  }

  const handleDeleteStatus = async (id) => {
    try {
      await statusApi.delete(id)
      fetchStatuses()
      toast.success('Status deleted')
      setDeleteStatusTarget(null)
    } catch (e) {
      if (e?.code === 1014 || e?.message?.includes('being used')) {
        toast.error('Cannot delete: status is being used by tasks')
      } else {
        toast.error(e?.message || 'Failed to delete status')
      }
      setDeleteStatusTarget(null)
    }
  }

  const handleUpdateProject = async () => {
    if (!projectForm.name.trim()) return
    try {
      await projectApi.update(projectId, { ...projectForm, teamId })
      setProject(prev => ({ ...prev, ...projectForm }))
      setEditingProject(false)
      toast.success('Project updated')
    } catch (e) { toast.error(e?.message || 'Failed to update project') }
  }

  return (
    <Layout>
      <div className="fade-in">
        <div className="flex items-center justify-between mb-1">
          <div className="flex items-center gap-3 flex-1 mr-4">
            {editingProject ? (
              <div className="flex flex-col gap-2 flex-1">
                <div className="flex items-center gap-2">
                  <input className="input text-xl font-display font-bold py-1 flex-1"
                    value={projectForm.name}
                    onChange={e => setProjectForm(p => ({ ...p, name: e.target.value }))}
                    autoFocus onKeyDown={e => e.key === 'Enter' && handleUpdateProject()} />
                  <button onClick={handleUpdateProject} className="text-primary"><HiCheck className="text-xl" /></button>
                  <button onClick={() => setEditingProject(false)} className="text-slate-400"><HiX className="text-xl" /></button>
                </div>
                <input className="input text-sm" value={projectForm.description}
                  onChange={e => setProjectForm(p => ({ ...p, description: e.target.value }))}
                  placeholder="Description..." />
              </div>
            ) : (
              <div className="flex flex-col gap-1">
                <div className="flex items-center gap-3">
                  <h1 className="font-display font-bold text-3xl text-primary">{project?.name || 'Project'}</h1>
                  {isAdmin
                    ? <button onClick={() => setEditingProject(true)} className="text-slate-400 hover:text-primary"><HiPencil /></button>
                    : <HiLockClosed className="text-slate-300" />}
                </div>
                {project?.description && (
                  <p className="text-sm text-slate-500">Description: {project.description}</p>
                )}
              </div>
            )}
          </div>
          <button onClick={() => setView(v => v === 'tasklists' ? 'statuses' : 'tasklists')} className="btn-primary flex-shrink-0">
            {view === 'tasklists' ? 'Status' : 'Task list'}
          </button>
        </div>

        <div className="mt-4">
          {view === 'tasklists' && (
            <div className="card overflow-hidden">
              <div className="grid grid-cols-12 px-4 py-3 bg-surface border-b border-border text-xs font-semibold text-slate-500 uppercase tracking-wide">
                <div className="col-span-6">Name</div>
                <div className="col-span-2 text-center">Update</div>
                <div className="col-span-2 text-center">Del</div>
                <div className="col-span-2 text-right">Created at</div>
              </div>
              {taskLists.length === 0 && <div className="p-8 text-center text-slate-400 text-sm">No task lists yet</div>}
              {taskLists.map(tl => (
                <div key={tl.id} className="grid grid-cols-12 items-center px-4 py-3 border-b border-border last:border-0 hover:bg-surface">
                  <div className="col-span-6">
                    {editingId === tl.id
                      ? <input className="input py-1 text-sm" value={editName}
                          onChange={e => setEditName(e.target.value)}
                          onKeyDown={e => { if (e.key === 'Enter') handleUpdateTL(tl.id); if (e.key === 'Escape') setEditingId(null) }}
                          autoFocus />
                      : <button onClick={() => { localStorage.setItem('selectedProjectId', projectId); navigate(`/task-lists/${tl.id}`) }}
                          className="text-primary font-medium text-sm hover:underline text-left">{tl.name}</button>}
                  </div>
                  <div className="col-span-2 flex justify-center">
                    {editingId === tl.id
                      ? <button onClick={() => handleUpdateTL(tl.id)} className="text-primary"><HiCheck className="text-lg" /></button>
                      : isAdmin
                        ? <button onClick={() => { setEditingId(tl.id); setEditName(tl.name) }} className="text-slate-400 hover:text-primary"><HiPencil className="text-lg" /></button>
                        : <HiLockClosed className="text-slate-200" />}
                  </div>
                  <div className="col-span-2 flex justify-center">
                    {editingId === tl.id
                      ? <button onClick={() => setEditingId(null)} className="text-slate-400"><HiX className="text-lg" /></button>
                      : isAdmin
                        ? <button onClick={() => setDeleteTarget(tl)} className="text-slate-400 hover:text-red-500"><HiTrash className="text-lg" /></button>
                        : <HiLockClosed className="text-slate-200" />}
                  </div>
                  <div className="col-span-2 text-right text-sm text-slate-500">
                    {tl.createdAt ? dayjs(tl.createdAt).format('D/M/YYYY') : '—'}
                  </div>
                </div>
              ))}
              {/* Tạo task list: cả admin và member */}
              <button onClick={() => setShowCreateTL(true)}
                className="w-full py-3 text-sm text-primary hover:bg-surface flex items-center justify-center gap-1">
                <HiPlus /> Add a task list
              </button>
            </div>
          )}

          {view === 'statuses' && (
            <div className="card overflow-hidden">
              <div className="grid grid-cols-12 px-4 py-3 bg-surface border-b border-border text-xs font-semibold text-slate-500 uppercase tracking-wide">
                <div className="col-span-8">Status</div>
                <div className="col-span-2 text-center">Update</div>
                <div className="col-span-2 text-center">Del</div>
              </div>
              {statuses.length === 0 && <div className="p-8 text-center text-slate-400 text-sm">No statuses</div>}
              {statuses.map(s => (
                <div key={s.id} className="grid grid-cols-12 items-center px-4 py-3 border-b border-border last:border-0 hover:bg-surface">
                  <div className="col-span-8">
                    {editingStatusId === s.id
                      ? <input className="input py-1 text-sm" value={editStatusName}
                          onChange={e => setEditStatusName(e.target.value)}
                          onKeyDown={e => { if (e.key === 'Enter') handleUpdateStatus(s.id); if (e.key === 'Escape') setEditingStatusId(null) }}
                          autoFocus />
                      : <span className="text-sm font-medium text-slate-700">{s.name || s.status}</span>}
                  </div>
                  <div className="col-span-2 flex justify-center">
                    {editingStatusId === s.id
                      ? <button onClick={() => handleUpdateStatus(s.id)} className="text-primary"><HiCheck className="text-lg" /></button>
                      : isAdmin
                        ? <button onClick={() => { setEditingStatusId(s.id); setEditStatusName(s.name || s.status) }} className="text-slate-400 hover:text-primary"><HiPencil className="text-lg" /></button>
                        : <HiLockClosed className="text-slate-200" />}
                  </div>
                  <div className="col-span-2 flex justify-center">
                    {editingStatusId === s.id
                      ? <button onClick={() => setEditingStatusId(null)} className="text-slate-400"><HiX className="text-lg" /></button>
                      : isAdmin
                        ? <button onClick={() => setDeleteStatusTarget(s)} className="text-slate-400 hover:text-red-500"><HiTrash className="text-lg" /></button>
                        : <HiLockClosed className="text-slate-200" />}
                  </div>
                </div>
              ))}
              {isAdmin && (
                <button onClick={() => setShowCreateStatus(true)}
                  className="w-full py-3 text-sm text-primary hover:bg-surface flex items-center justify-center gap-1">
                  <HiPlus /> Add a status
                </button>
              )}
            </div>
          )}
        </div>
      </div>

      <Modal open={showCreateTL} onClose={() => setShowCreateTL(false)} title="Create Task List" size="sm">
        <div className="space-y-4">
          <input className="input" placeholder="Task list name" value={newTLName}
            onChange={e => setNewTLName(e.target.value)} onKeyDown={e => e.key === 'Enter' && handleCreateTL()} autoFocus />
          <div className="flex gap-2 justify-end">
            <button onClick={() => setShowCreateTL(false)} className="btn-secondary">Cancel</button>
            <button onClick={handleCreateTL} className="btn-primary">Create</button>
          </div>
        </div>
      </Modal>

      <Modal open={showCreateStatus} onClose={() => setShowCreateStatus(false)} title="Create Status" size="sm">
        <div className="space-y-4">
          <input className="input" placeholder="e.g. In Progress" value={newStatusName}
            onChange={e => setNewStatusName(e.target.value)} onKeyDown={e => e.key === 'Enter' && handleCreateStatus()} autoFocus />
          <div className="flex gap-2 justify-end">
            <button onClick={() => setShowCreateStatus(false)} className="btn-secondary">Cancel</button>
            <button onClick={handleCreateStatus} className="btn-primary">Create</button>
          </div>
        </div>
      </Modal>

      <ConfirmDialog open={!!deleteTarget} onClose={() => setDeleteTarget(null)}
        onConfirm={() => handleDeleteTL(deleteTarget?.id)}
        title="Delete Task List" message={`Delete "${deleteTarget?.name}"?`} />

      <ConfirmDialog open={!!deleteStatusTarget} onClose={() => setDeleteStatusTarget(null)}
        onConfirm={() => handleDeleteStatus(deleteStatusTarget?.id)}
        title="Delete Status" message={`Delete status "${deleteStatusTarget?.name || deleteStatusTarget?.status}"?`} />
    </Layout>
  )
}