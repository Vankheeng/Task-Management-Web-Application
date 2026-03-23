import { useState, useEffect } from 'react'
import Modal from '../common/Modal'
import teamMemberApi from '../../api/teamMemberApi'
import taskAssignmentApi from '../../api/taskAssignmentApi'
import { HiPlus } from 'react-icons/hi'

export default function AssignTaskModal({ open, onClose, taskId, teamId, existingAssignments = [], onAssigned }) {
  const [members, setMembers] = useState([])
  const [loading, setLoading] = useState(false)

  useEffect(() => {
    if (open && teamId) {
      teamMemberApi.getByTeamId(teamId)
        .then(res => setMembers(res.result || []))
        .catch(() => {})
    }
  }, [open, teamId])

  const assignedUserIds = existingAssignments.map(a => a.user?.id)

  const handleAssign = async (userId) => {
    setLoading(true)
    try {
      await taskAssignmentApi.assign({ taskId, userId })
      onAssigned?.()
    } catch {} finally { setLoading(false) }
  }

  return (
    <Modal open={open} onClose={onClose} title="Assign Member" size="sm">
      <div className="space-y-2 max-h-64 overflow-y-auto">
        {members.length === 0 && (
          <p className="text-sm text-slate-400 text-center py-4">No members in this team</p>
        )}
        {members.map((m, idx) => {
          const alreadyAssigned = assignedUserIds.includes(m.user?.id)
          return (
            <div key={m.id || idx} className="flex items-center justify-between p-3 rounded-lg border border-border hover:bg-surface">
              <div>
                <p className="text-sm font-medium">{m.user?.fullName}</p>
                <p className="text-xs text-slate-500">{m.user?.username} · {m.role}</p>
              </div>
              {alreadyAssigned ? (
                <span className="badge bg-green-100 text-green-700 text-xs">Assigned</span>
              ) : (
                <button
                  onClick={() => handleAssign(m.user?.id)}
                  disabled={loading}
                  className="btn-primary text-xs py-1 px-2"
                >
                  <HiPlus className="text-xs" /> Assign
                </button>
              )}
            </div>
          )
        })}
      </div>
    </Modal>
  )
}