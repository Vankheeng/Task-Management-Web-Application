import { HiTrash } from 'react-icons/hi'

export default function TeamMemberRow({ member, onUpdateRole, onRemove }) {
  return (
    <div className="grid grid-cols-12 items-center px-4 py-3 border-b border-border last:border-0 hover:bg-surface transition-colors">
      <div className="col-span-3 text-sm font-medium text-slate-700">
        {member.user?.username}
      </div>
      <div className="col-span-3 text-sm text-slate-600">
        {member.user?.fullName}
      </div>
      <div className="col-span-3 text-sm text-slate-500 truncate">
        {member.user?.email}
      </div>
      <div className="col-span-2">
        <select
          value={member.role}
          onChange={e => onUpdateRole(member, e.target.value)}
          className="input py-1 text-xs"
        >
          <option value="ADMIN">Admin</option>
          <option value="MEMBER">Member</option>
        </select>
      </div>
      <div className="col-span-1 flex justify-end">
        <button
          onClick={() => onRemove(member.id)}
          className="text-slate-400 hover:text-red-500 transition-colors"
        >
          <HiTrash />
        </button>
      </div>
    </div>
  )
}