import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import teamApi from '../../api/teamApi'
import teamMemberApi from '../../api/teamMemberApi'
import projectApi from '../../api/projectApi'
import Layout from '../../components/common/Layout'
import Modal from '../../components/common/Modal'
import ConfirmDialog from '../../components/common/ConfirmDialog'
import { useToast } from '../../components/common/Toast'
import { useAuth } from '../../context/AuthContext'
import { HiPlus, HiPencil, HiTrash, HiCheck, HiX, HiUserAdd, HiLockClosed } from 'react-icons/hi'
import dayjs from 'dayjs'

export default function TeamDetailPage() {
  const { teamId } = useParams()
  const navigate = useNavigate()
  const toast = useToast()
  const { user } = useAuth()

  const [members, setMembers] = useState([])
  const [projects, setProjects] = useState([])
  const [myRole, setMyRole] = useState(() => localStorage.getItem('selectedTeamRole') || null) // 'ADMIN' | 'MEMBER'
  const [isMember, setIsMember] = useState(true)
  const [view, setView] = useState('projects')
  const [editingProjectId, setEditingProjectId] = useState(null)
  const [editProjectName, setEditProjectName] = useState('')
  const [searchUsername, setSearchUsername] = useState('')
  const [searchEmail, setSearchEmail] = useState('')
  const [searchResult, setSearchResult] = useState(null)
  const [showCreateProject, setShowCreateProject] = useState(false)
  const [newProject, setNewProject] = useState({ name: '', description: '' })
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [editingTeamName, setEditingTeamName] = useState(false)
  const [teamName, setTeamName] = useState(localStorage.getItem('selectedTeamName') || '')
  const [searchMode, setSearchMode] = useState('username')
  const [showLeaveConfirm, setShowLeaveConfirm] = useState(false)
  const [leaveWarning, setLeaveWarning] = useState('')

  const fetchAll = async () => {
    try {
      const [membersRes, projectsRes] = await Promise.all([
        teamMemberApi.getByTeamId(teamId),
        projectApi.getByTeamId(teamId),
      ])
      const memberList = membersRes.result || []
      setMembers(memberList)
      setProjects(projectsRes.result || [])

      // Kiểm tra membership và role
      const me = memberList.find(m => m.user?.id === user?.id)
      if (me?.role) localStorage.setItem('selectedTeamRole', me.role)
      if (!me) {
        // Không còn là thành viên → redirect
        setIsMember(false)
        toast.error('You are not a member of this team')
        navigate('/')
        return
      }
      setMyRole(me.role)
      localStorage.setItem('selectedTeamRole', me.role)
      setIsMember(true)
    } catch (e) {
      const code = e?.code
      if (code === 1003 || e?.status === 403) {
        toast.error('You do not have access to this team')
        navigate('/')
      }
    }
  }

  useEffect(() => { fetchAll() }, [teamId])

  const isAdmin = myRole === 'ADMIN'

  const handleSearchUser = async () => {
    const query = searchMode === 'username' ? searchUsername.trim() : searchEmail.trim()
    if (!query) return
    try {
      const res = searchMode === 'username'
        ? await teamMemberApi.searchUser(query)
        : await teamMemberApi.searchByEmail(query)
      setSearchResult(res.result)
    } catch {
      setSearchResult(null)
      toast.error('User not found')
    }
  }

  const handleAddMember = async () => {
    if (!isAdmin) { toast.error('Only admins can add members'); return }
    if (!searchResult) return
    // Kiểm tra user đã có trong nhóm chưa
    const alreadyMember = members.some(m => m.user?.id === searchResult.id)
    if (alreadyMember) {
      toast.error('This user is already a member of this team')
      return
    }
    try {
      await teamMemberApi.addMember({ userId: searchResult.id, teamId, role: 'MEMBER' })
      setSearchUsername(''); setSearchEmail(''); setSearchResult(null)
      fetchAll()
      toast.success('Member added successfully')
    } catch (e) {
      toast.error(e?.message || 'Failed to add member')
    }
  }

  const confirmRemoveMember = (memberId, memberUserId) => {
    if (!isAdmin) { toast.error('Only admins can remove members'); return }

    const isSelf = memberUserId === user?.id
    const adminCount = members.filter(m => m.role === 'ADMIN').length
    const targetMember = members.find(m => m.id === memberId)
    const targetIsAdmin = targetMember?.role === 'ADMIN'

    if (isSelf) {
      // Xóa chính mình
      if (members.length === 1) {
        // Thành viên cuối → xóa team
        setLeaveWarning('You are the last member. Leaving will permanently delete this team and all its data.')
      } else if (adminCount === 1) {
        // Admin cuối, còn member khác → không cho rời
        toast.error('You are the last admin. Assign another admin before leaving.')
        return
      } else {
        setLeaveWarning('You will be removed from this team.')
      }
      setShowLeaveConfirm({ memberId, memberUserId })
    } else {
      // Xóa người khác
      if (targetIsAdmin && adminCount === 1) {
        // Xóa admin cuối, còn member khác → không cho
        toast.error('Cannot remove the last admin while other members exist. Assign another admin first.')
        return
      }
      // Xác nhận bình thường
      setLeaveWarning('Remove "' + (targetMember?.user?.fullName || 'this member') + '" from the team?')
      setShowLeaveConfirm({ memberId, memberUserId })
    }
  }

  const handleRemoveMember = async (memberId, memberUserId) => {
    try {
      await teamMemberApi.removeMember(memberId)
      const isSelf = memberUserId === user?.id
      if (isSelf) {
        localStorage.removeItem('selectedTeamId')
        localStorage.removeItem('selectedTeamName')
        localStorage.removeItem('selectedProjectId')
        navigate('/')
        return
      }
      fetchAll()
      toast.success('Member removed')
    } catch (e) {
      const msg = e?.message || ''
      if (msg.includes('last admin') || msg.includes('LAST_ADMIN') || e?.code === 1015) {
        toast.error('Cannot remove: last admin must assign another admin first')
      } else {
        toast.error(e?.message || 'Failed to remove member')
      }
    }
  }

  const handleUpdateRole = async (member, role) => {
    if (!isAdmin) { toast.error('Only admins can change roles'); return }
    try {
      await teamMemberApi.updateMember(member.id, { userId: member.user.id, teamId, role })
      fetchAll()
      toast.success('Role updated')
    } catch (e) {
      toast.error(e?.message || 'Failed to update role')
    }
  }

  const handleCreateProject = async () => {
    if (!isAdmin) { toast.error('Only admins can create projects'); return }
    if (!newProject.name.trim()) return
    try {
      await projectApi.create({ ...newProject, teamId })
      setShowCreateProject(false)
      setNewProject({ name: '', description: '' })
      fetchAll()
      toast.success('Project created')
    } catch (e) {
      toast.error(e?.message || 'Failed to create project')
    }
  }

  const handleUpdateProject = async (id) => {
    if (!isAdmin) { toast.error('Only admins can update projects'); return }
    try {
      await projectApi.update(id, { name: editProjectName, teamId })
      setEditingProjectId(null)
      fetchAll()
      toast.success('Project updated')
    } catch (e) {
      toast.error(e?.message || 'Failed to update project')
    }
  }

  const handleDeleteProject = async (id) => {
    if (!isAdmin) { toast.error('Only admins can delete projects'); return }
    try {
      await projectApi.delete(id)
      fetchAll()
      toast.success('Project deleted')
      setDeleteTarget(null)
    } catch (e) {
      toast.error(e?.message || 'Failed to delete project')
    }
  }

  const handleUpdateTeamName = async () => {
    if (!isAdmin) { toast.error('Only admins can update the team name'); return }
    try {
      await teamApi.updateTeam(teamId, { name: teamName })
      localStorage.setItem('selectedTeamName', teamName)
      setEditingTeamName(false)
      toast.success('Team name updated')
    } catch (e) {
      toast.error(e?.message || 'Failed to update team name')
    }
  }

  if (!isMember) return null

  return (
    <Layout>
      <div className="fade-in">
        {/* Header */}
        <div className="flex items-center justify-between mb-2">
          <div className="flex items-center gap-3">
            {editingTeamName && isAdmin ? (
              <div className="flex items-center gap-2">
                <input className="input text-2xl font-display font-bold py-1" value={teamName}
                  onChange={e => setTeamName(e.target.value)}
                  onKeyDown={e => e.key === 'Enter' && handleUpdateTeamName()} autoFocus />
                <button onClick={handleUpdateTeamName} className="text-primary"><HiCheck /></button>
                <button onClick={() => setEditingTeamName(false)} className="text-slate-400"><HiX /></button>
              </div>
            ) : (
              <>
                <h1 className="font-display font-bold text-3xl text-primary">{teamName || 'Team'}</h1>
                {isAdmin && (
                  <button onClick={() => setEditingTeamName(true)} className="text-slate-400 hover:text-primary">
                    <HiPencil />
                  </button>
                )}
              </>
            )}
            {/* Role badge */}
            <span className={`badge text-xs ${isAdmin ? 'bg-primary-light text-primary' : 'bg-slate-100 text-slate-500'}`}>
              {myRole}
            </span>
          </div>

          <div className="flex gap-2">
            {view === 'projects' && (
              <>
                {isAdmin && (
                  <button onClick={() => setView('members')} className="btn-primary">
                    <HiUserAdd /> Add member
                  </button>
                )}
                {!isAdmin && (
                  <button onClick={() => setView('members')} className="btn-secondary">
                    View members
                  </button>
                )}
                {isAdmin && (
                  <button onClick={() => setShowCreateProject(true)} className="btn-secondary">
                    <HiPlus /> Add project
                  </button>
                )}
              </>
            )}
            {view === 'members' && (
              <button onClick={() => setView('projects')} className="btn-secondary">
                Back to Projects
              </button>
            )}
          </div>
        </div>

        <button onClick={() => setView('members')}
          className="text-sm text-slate-500 mb-6 hover:text-primary hover:underline transition-colors">
          {members.length} members
        </button>

        {/* Members view */}
        {view === 'members' ? (
          <div className="card overflow-hidden">
            {/* Search - chỉ admin mới add được */}
            {isAdmin && (
              <div className="p-4 border-b border-border space-y-3">
                <div className="flex gap-2">
                  {['username', 'email'].map(mode => (
                    <button key={mode}
                      onClick={() => { setSearchMode(mode); setSearchResult(null) }}
                      className={`text-xs px-3 py-1 rounded-full border transition-colors
                        ${searchMode === mode ? 'bg-primary text-white border-primary' : 'border-border text-slate-500 hover:border-primary'}`}>
                      By {mode}
                    </button>
                  ))}
                </div>
                <div className="flex items-center gap-3">
                  <input className="input flex-1"
                    placeholder={`Enter ${searchMode}`}
                    type={searchMode === 'email' ? 'email' : 'text'}
                    value={searchMode === 'username' ? searchUsername : searchEmail}
                    onChange={e => searchMode === 'username' ? setSearchUsername(e.target.value) : setSearchEmail(e.target.value)}
                    onKeyDown={e => e.key === 'Enter' && handleSearchUser()} />
                  <button onClick={handleSearchUser} className="btn-primary">Search</button>
                </div>
                {searchResult && (() => {
                  const alreadyMember = members.some(m => m.user?.id === searchResult.id)
                  return (
                    <div className={`p-3 border rounded-lg flex items-center justify-between
                      ${alreadyMember ? 'bg-slate-50 border-slate-200' : 'bg-primary-light border-primary/20'}`}>
                      <div>
                        <p className="font-medium text-sm">{searchResult.fullName}</p>
                        <p className="text-xs text-slate-500">@{searchResult.username} · {searchResult.email}</p>
                      </div>
                      {alreadyMember
                        ? <span className="badge bg-slate-100 text-slate-500 text-xs">Already a member</span>
                        : <button onClick={handleAddMember} className="btn-primary text-xs py-1">Add</button>}
                    </div>
                  )
                })()}
              </div>
            )}

            <div className="grid grid-cols-12 px-4 py-2 bg-surface text-xs font-semibold text-slate-500 uppercase tracking-wide border-b border-border">
              <div className="col-span-3">Username</div>
              <div className="col-span-3">Full name</div>
              <div className="col-span-3">Email</div>
              <div className="col-span-2">Role</div>
              <div className="col-span-1"></div>
            </div>

            {members.map((m, idx) => (
              <div key={m.id || m.user?.id || idx}
                className="grid grid-cols-12 items-center px-4 py-3 border-b border-border last:border-0 hover:bg-surface">
                <div className="col-span-3 text-sm font-medium">{m.user?.username}</div>
                <div className="col-span-3 text-sm text-slate-600">{m.user?.fullName}</div>
                <div className="col-span-3 text-sm text-slate-500 truncate">{m.user?.email}</div>
                <div className="col-span-2">
                  {isAdmin ? (
                    <select value={m.role} onChange={e => handleUpdateRole(m, e.target.value)}
                      className="input py-1 text-xs">
                      <option value="ADMIN">Admin</option>
                      <option value="MEMBER">Member</option>
                    </select>
                  ) : (
                    <span className={`badge text-xs ${m.role === 'ADMIN' ? 'bg-primary-light text-primary' : 'bg-slate-100 text-slate-500'}`}>
                      {m.role}
                    </span>
                  )}
                </div>
                <div className="col-span-1 flex justify-end">
                  {isAdmin ? (
                    <button onClick={() => m.id && confirmRemoveMember(m.id, m.user?.id)}
                      className="text-slate-400 hover:text-red-500"><HiTrash /></button>
                  ) : (
                    <HiLockClosed className="text-slate-200" />
                  )}
                </div>
              </div>
            ))}
          </div>
        ) : (
          <div className="card overflow-hidden">
            <div className="grid grid-cols-12 px-4 py-3 bg-surface border-b border-border text-xs font-semibold text-slate-500 uppercase tracking-wide">
              <div className="col-span-6">Name</div>
              <div className="col-span-4">Created at</div>
              <div className="col-span-2 text-right">Actions</div>
            </div>

            {projects.length === 0 && (
              <div className="p-8 text-center text-slate-400 text-sm">No projects yet</div>
            )}

            {projects.map(project => (
              <div key={project.id} className="grid grid-cols-12 items-center px-4 py-3 border-b border-border last:border-0 hover:bg-surface">
                <div className="col-span-6">
                  {editingProjectId === project.id ? (
                    <input className="input py-1 text-sm" value={editProjectName}
                      onChange={e => setEditProjectName(e.target.value)}
                      onKeyDown={e => { if (e.key === 'Enter') handleUpdateProject(project.id); if (e.key === 'Escape') setEditingProjectId(null) }}
                      autoFocus />
                  ) : (
                    <button
                      onClick={() => { localStorage.setItem('selectedProjectId', project.id); navigate(`/projects/${project.id}`) }}
                      className="text-primary font-medium text-sm hover:underline">
                      {project.name}
                    </button>
                  )}
                </div>
                <div className="col-span-4 text-sm text-slate-500">
                  {dayjs(project.createdAt).format('D/M/YYYY')}
                </div>
                <div className="col-span-2 flex justify-end gap-2">
                  {isAdmin && (
                    editingProjectId === project.id ? (
                      <>
                        <button onClick={() => handleUpdateProject(project.id)} className="text-primary"><HiCheck /></button>
                        <button onClick={() => setEditingProjectId(null)} className="text-slate-400"><HiX /></button>
                      </>
                    ) : (
                      <>
                        <button onClick={() => { setEditingProjectId(project.id); setEditProjectName(project.name) }}
                          className="text-slate-400 hover:text-primary"><HiPencil /></button>
                        <button onClick={() => setDeleteTarget(project)}
                          className="text-slate-400 hover:text-red-500"><HiTrash /></button>
                      </>
                    )
                  )}
                  {!isAdmin && <HiLockClosed className="text-slate-200" />}
                </div>
              </div>
            ))}

            {isAdmin && (
              <button onClick={() => setShowCreateProject(true)}
                className="w-full py-3 text-sm text-primary hover:bg-surface flex items-center justify-center gap-1">
                <HiPlus /> Add a project
              </button>
            )}
          </div>
        )}
      </div>

      <Modal open={showCreateProject} onClose={() => setShowCreateProject(false)} title="Create Project">
        <div className="space-y-4">
          <input className="input" placeholder="Project name" value={newProject.name}
            onChange={e => setNewProject(p => ({ ...p, name: e.target.value }))} autoFocus />
          <textarea className="input resize-none" rows={3} placeholder="Description (optional)"
            value={newProject.description}
            onChange={e => setNewProject(p => ({ ...p, description: e.target.value }))} />
          <div className="flex gap-2 justify-end">
            <button onClick={() => setShowCreateProject(false)} className="btn-secondary">Cancel</button>
            <button onClick={handleCreateProject} className="btn-primary">Create</button>
          </div>
        </div>
      </Modal>

      <ConfirmDialog
        open={!!showLeaveConfirm}
        onClose={() => setShowLeaveConfirm(false)}
        onConfirm={() => {
          handleRemoveMember(showLeaveConfirm.memberId, showLeaveConfirm.memberUserId)
          setShowLeaveConfirm(false)
        }}
        title={
          showLeaveConfirm?.memberUserId === user?.id
            ? members.length === 1 ? 'Delete Team' : 'Leave Team'
            : 'Remove Member'
        }
        message={leaveWarning}
      />

      <ConfirmDialog open={!!deleteTarget} onClose={() => setDeleteTarget(null)}
        onConfirm={() => handleDeleteProject(deleteTarget?.id)}
        title="Delete Project"
        message={`Delete "${deleteTarget?.name}"? All task lists and tasks will be removed.`} />
    </Layout>
  )
}