export default function EmptyState({ icon, message, action }) {
  return (
    <div className="flex flex-col items-center justify-center py-16 text-center">
      {icon && <div className="text-5xl text-slate-200 mb-4">{icon}</div>}
      <p className="text-slate-400 text-sm mb-4">{message}</p>
      {action && action}
    </div>
  )
}
