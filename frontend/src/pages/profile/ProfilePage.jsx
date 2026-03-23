import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import projectApi from '../../api/projectApi'
import taskListApi from '../../api/taskListApi'
import Layout from '../../components/common/Layout'
import Modal from '../../components/common/Modal'
import ConfirmDialog from '../../components/common/ConfirmDialog'
import { HiPlus, HiPencil, HiTrash, HiSave, HiX } from 'react-icons/hi'
import dayjs from 'dayjs'

export default function ProjectDetailPage() {
  const { projectId } = useParams()
  const navigate = useNavigate()
  const [project, setProject] = useState(null)
  const [taskLists, setTaskLists] = useState([])
  const [editingId, setEditingId] = useState(null)
  const [editName, setEditName] = useState('')
  const [showCreate, setShowCreate] = useState(false)
  const [newName, setNewName] = useState('')
  const [deleteTarget, setDeleteTarget] = useState(null)
  const [editingProject, setEditingProject] = useState(false)
  const [projectForm, setProjectForm] = useState({ name: '', description: '' })

  const fetchAll = async () => {
    try {
      const [tlRes] = await Promise.all([
        taskListApi.getByProjectId(projectId),
      ])
      setTaskLists(tlRes.result || [])
    } catch {}
  }

  useEffect(() => { fetchAll() }, [projectId])

  const handleCreate = async () => {
    if (!newName.trim()) return
    await taskListApi.create({ name: newName, projectId })
    setNewName('')
    setShowCreate(false)
    fetchAll()
  }

  const handleUpdate = async (id) => {
    await taskListApi.update(id, { name: editName, projectId })
    setEditingId(null)
    fetchAll()
  }

  const handleDelete = async (id) => {
    await taskListApi.delete(id)
    fetchAll()
  }

  return (
    <Layout>
      <div className="fade-in">
        {/* Header */}
        <div className="flex items-center gap-3 mb-1">
          {editingProject ? (
            <div className="flex items-center gap-2 flex-1">
              <input className="input text-2xl font-display font-bold py-1"
                value={projectForm.name}
                onChange={e => setProjectForm(p => ({ ...p, name: e.target.value }))} autoFocus />
              <button onClick={async () => {
                await projectApi.update(projectId, projectForm)
                setEditingProject(false)
                fetchAll()
              }} className="text-primary"><HiSave /></button>
              <button onClick={() => setEditingProject(false)} className="text-slate-400"><HiX /></button>
            </div>
          ) : (
            <>
              <h1 className="font-display font-bold text-3xl text-primary">{project?.name || 'Project'}</h1>
              <button onClick={() => { setEditingProject(true); setProjectForm({ name: project?.name || '', description: project?.description || '' }) }}
                className="text-slate-400 hover:text-primary"><HiPencil /></button>
            </>
          )}
        </div>
        {project?.description && (
          <p className="text-sm text-slate-500 mb-6">Description: {project.description}</p>
        )}

        <div className="card overflow-hidden mt-4">
          <div className="grid grid-cols-12 px-4 py-3 bg-surface border-b border-border text-xs font-semibold text-slate-500 uppercase tracking-wide">
            <div className="col-span-6">Name</div>
            <div className="col-span-2 text-right">Update</div>
            <div className="col-span-2 text-right">Del</div>
            <div className="col-span-2 text-right">Created at</div>
          </div>

          {taskLists.map(tl => (
            <div key={tl.id} className="grid grid-cols-12 items-center px-4 py-3 border-b border-border last:border-0 hover:bg-surface">
              <div className="col-span-6">
                {editingId === tl.id ? (
                  <input className="input py-1 text-sm" value={editName}
                    onChange={e => setEditName(e.target.value)}
                    onKeyDown={e => e.key === 'Enter' && handleUpdate(tl.id)} autoFocus />
                ) : (
                  <button onClick={() => navigate(`/task-lists/${tl.id}`)}
                    className="text-primary font-medium text-sm hover:underline">{tl.name}</button>
                )}
              </div>
              <div className="col-span-2 flex justify-end">
                {editingId === tl.id ? (
                  <button onClick={() => handleUpdate(tl.id)} className="text-primary"><HiSave /></button>
                ) : (
                  <button onClick={() => { setEditingId(tl.id); setEditName(tl.name) }}
                    className="text-slate-400 hover:text-primary"><HiPencil /></button>
                )}
              </div>
              <div className="col-span-2 flex justify-end">
                {editingId === tl.id ? (
                  <button onClick={() => setEditingId(null)} className="text-slate-400"><HiX /></button>
                ) : (
                  <button onClick={() => setDeleteTarget(tl)} className="text-slate-400 hover:text-red-500"><HiTrash /></button>
                )}
              </div>
              <div className="col-span-2 text-right text-sm text-slate-500">
                {dayjs(tl.createdAt).format('D/M/YYYY')}
              </div>
            </div>
          ))}

          <button onClick={() => setShowCreate(true)}
            className="w-full py-3 text-sm text-primary hover:bg-surface flex items-center justify-center gap-1">
            <HiPlus /> Add a task list
          </button>
        </div>
      </div>

      <Modal open={showCreate} onClose={() => setShowCreate(false)} title="Create Task List" size="sm">
        <div className="space-y-4">
          <input className="input" placeholder="Task list name" value={newName}
            onChange={e => setNewName(e.target.value)}
            onKeyDown={e => e.key === 'Enter' && handleCreate()} autoFocus />
          <div className="flex gap-2 justify-end">
            <button onClick={() => setShowCreate(false)} className="btn-secondary">Cancel</button>
            <button onClick={handleCreate} className="btn-primary">Create</button>
          </div>
        </div>
      </Modal>

      <ConfirmDialog open={!!deleteTarget} onClose={() => setDeleteTarget(null)}
        onConfirm={() => handleDelete(deleteTarget?.id)}
        title="Delete Task List"
        message={`Delete "${deleteTarget?.name}"? All tasks will be removed.`} />
    </Layout>
  )
}