import { useState } from 'react'
import taskAttachmentApi from '../../api/taskAttachmentApi'
import { HiPaperClip, HiPlus, HiTrash, HiPencil, HiSave, HiX } from 'react-icons/hi'

export default function AttachmentPanel({ taskId, attachments = [], onRefetch }) {
  const [showForm, setShowForm] = useState(false)
  const [form, setForm] = useState({ name: '', url: '' })
  const [editingId, setEditingId] = useState(null)
  const [editForm, setEditForm] = useState({ name: '', url: '' })
  const [loading, setLoading] = useState(false)

  const handleAdd = async () => {
    if (!form.name.trim() || !form.url.trim()) return
    setLoading(true)
    try {
      await taskAttachmentApi.create({ fileName: form.name, fileUrl: form.url, taskId })
      setForm({ name: '', url: '' })
      setShowForm(false)
      onRefetch?.()
    } finally { setLoading(false) }
  }

  const handleUpdate = async (id) => {
    await taskAttachmentApi.update(id, { fileName: editForm.name, fileUrl: editForm.url, taskId })
    setEditingId(null)
    onRefetch?.()
  }

  const handleDelete = async (id) => {
    await taskAttachmentApi.delete(id)
    onRefetch?.()
  }

  return (
    <div className="card p-4">
      {/* Header */}
      <div className="flex items-center justify-between mb-3">
        <h3 className="font-semibold text-sm text-slate-700 flex items-center gap-1.5">
          <HiPaperClip className="text-slate-400" />
          Attachment
        </h3>
        <button
          onClick={() => setShowForm(p => !p)}
          className="text-xs text-primary hover:underline flex items-center gap-0.5"
        >
          <HiPlus className="text-xs" /> New
        </button>
      </div>

      {/* Add form */}
      {showForm && (
        <div className="mb-3 p-3 bg-surface rounded-lg border border-border space-y-2 fade-in">
          <p className="text-xs font-semibold text-slate-600">New attachment</p>
          <input className="input text-xs py-1.5" placeholder="Name"
            value={form.name} onChange={e => setForm(p => ({ ...p, name: e.target.value }))} />
          <input className="input text-xs py-1.5" placeholder="Url"
            value={form.url} onChange={e => setForm(p => ({ ...p, url: e.target.value }))} />
          <div className="flex justify-end gap-2">
            <button onClick={() => setShowForm(false)} className="text-xs text-slate-400 hover:text-slate-600">
              Cancel
            </button>
            <button onClick={handleAdd} disabled={loading}
              className="text-xs text-primary font-medium hover:underline">
              {loading ? 'Adding...' : 'Add'}
            </button>
          </div>
        </div>
      )}

      {/* List */}
      <div className="space-y-1">
        {attachments.length === 0 && !showForm && (
          <p className="text-xs text-slate-400 py-2">No attachments</p>
        )}
        {attachments.map(a => (
          <div key={a.id} className="group">
            {editingId === a.id ? (
              <div className="p-2 bg-surface rounded-lg border border-border space-y-2 fade-in">
                <p className="text-xs font-semibold text-slate-600">Update attachment</p>
                <input className="input text-xs py-1.5" placeholder="Name"
                  value={editForm.name} onChange={e => setEditForm(p => ({ ...p, name: e.target.value }))} />
                <input className="input text-xs py-1.5" placeholder="Url"
                  value={editForm.url} onChange={e => setEditForm(p => ({ ...p, url: e.target.value }))} />
                <div className="flex justify-end gap-2">
                  <button onClick={() => setEditingId(null)} className="text-xs text-slate-400">Cancel</button>
                  <button onClick={() => handleUpdate(a.id)} className="text-xs text-primary font-medium">Save</button>
                </div>
              </div>
            ) : (
              <div className="flex items-center justify-between py-1.5 border-b border-border last:border-0">
                <a href={a.fileUrl} target="_blank" rel="noreferrer"
                  className="text-xs text-primary hover:underline truncate flex-1 mr-2">
                  {a.fileName}
                </a>
                <div className="flex gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
                  <button onClick={() => { setEditingId(a.id); setEditForm({ name: a.fileName, url: a.fileUrl }) }}
                    className="text-slate-400 hover:text-primary">
                    <HiPencil className="text-xs" />
                  </button>
                  <button onClick={() => handleDelete(a.id)}
                    className="text-slate-400 hover:text-red-500">
                    <HiTrash className="text-xs" />
                  </button>
                </div>
              </div>
            )}
          </div>
        ))}
      </div>
    </div>
  )
}