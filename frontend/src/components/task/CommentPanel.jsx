import { useState } from 'react'
import commentApi from '../../api/commentApi'
import { useAuth } from '../../hooks/useAuth'
import { HiPaperAirplane, HiTrash, HiPencil, HiSave, HiX } from 'react-icons/hi'
import dayjs from 'dayjs'

export default function CommentPanel({ taskId, comments = [], onRefetch }) {
  const { user } = useAuth()
  const [newComment, setNewComment] = useState('')
  const [editingId, setEditingId] = useState(null)
  const [editContent, setEditContent] = useState('')
  const [submitting, setSubmitting] = useState(false)

  const handleSubmit = async () => {
    if (!newComment.trim()) return
    setSubmitting(true)
    try {
      await commentApi.create({ content: newComment.trim(), taskId })
      setNewComment('')
      onRefetch?.()
    } finally { setSubmitting(false) }
  }

  const handleUpdate = async (id) => {
    if (!editContent.trim()) return
    await commentApi.update(id, { content: editContent.trim(), taskId })
    setEditingId(null)
    onRefetch?.()
  }

  const handleDelete = async (id) => {
    await commentApi.delete(id)
    onRefetch?.()
  }

  return (
    <div className="card p-4 flex flex-col">
      <h3 className="font-semibold text-sm text-slate-700 mb-3">Comment</h3>

      {/* Comment list */}
      <div className="flex-1 space-y-3 max-h-72 overflow-y-auto mb-3">
        {comments.length === 0 && (
          <p className="text-xs text-slate-400 py-2">No comments yet</p>
        )}
        {comments.map(c => {
          const isOwn = c.createdBy?.id === user?.id
          return (
            <div key={c.id} className="group">
              <div className="flex items-center justify-between mb-0.5">
                <span className="text-xs font-medium text-slate-700">{c.createdBy?.fullName}</span>
                <div className="flex items-center gap-2">
                  <span className="text-xs text-slate-400">
                    {dayjs(c.createdAt).format('D/M/YYYY')}
                  </span>
                  <div className="flex gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
                    {isOwn && (
                      <button
                        onClick={() => { setEditingId(c.id); setEditContent(c.content) }}
                        className="text-slate-400 hover:text-primary"
                      >
                        <HiPencil className="text-xs" />
                      </button>
                    )}
                    <button
                      onClick={() => handleDelete(c.id)}
                      className="text-slate-400 hover:text-red-500"
                    >
                      <HiTrash className="text-xs" />
                    </button>
                  </div>
                </div>
              </div>

              {editingId === c.id ? (
                <div className="flex gap-2">
                  <input
                    className="input text-xs flex-1 py-1.5"
                    value={editContent}
                    onChange={e => setEditContent(e.target.value)}
                    onKeyDown={e => { if (e.key === 'Enter') handleUpdate(c.id); if (e.key === 'Escape') setEditingId(null) }}
                    autoFocus
                  />
                  <button onClick={() => handleUpdate(c.id)} className="text-primary"><HiSave className="text-sm" /></button>
                  <button onClick={() => setEditingId(null)} className="text-slate-400"><HiX className="text-sm" /></button>
                </div>
              ) : (
                <p className="text-xs text-slate-600 bg-surface px-3 py-2 rounded-lg">{c.content}</p>
              )}
            </div>
          )
        })}
      </div>

      {/* Input */}
      <div className="flex gap-2 pt-3 border-t border-border">
        <input
          className="input text-xs flex-1 py-1.5"
          placeholder="Write a comment..."
          value={newComment}
          onChange={e => setNewComment(e.target.value)}
          onKeyDown={e => e.key === 'Enter' && !e.shiftKey && handleSubmit()}
        />
        <button
          onClick={handleSubmit}
          disabled={submitting || !newComment.trim()}
          className="text-primary hover:text-primary-dark disabled:opacity-40 transition-colors"
        >
          <HiPaperAirplane className="rotate-90 text-lg" />
        </button>
      </div>
    </div>
  )
}