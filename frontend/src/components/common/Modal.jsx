import { useEffect } from 'react'
import { HiX } from 'react-icons/hi'

export default function Modal({ open, onClose, title, children, size = 'md' }) {
  useEffect(() => {
    if (open) document.body.style.overflow = 'hidden'
    else document.body.style.overflow = ''
    return () => { document.body.style.overflow = '' }
  }, [open])

  if (!open) return null

  const sizeClass = { sm: 'max-w-sm', md: 'max-w-md', lg: 'max-w-lg', xl: 'max-w-2xl' }[size]

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4">
      <div className="absolute inset-0 bg-slate-900/40 backdrop-blur-sm" onClick={onClose} />
      <div className={`relative bg-white rounded-2xl shadow-modal w-full ${sizeClass} fade-in`}>
        <div className="flex items-center justify-between px-6 py-4 border-b border-border">
          <h3 className="font-display font-semibold text-slate-800">{title}</h3>
          <button onClick={onClose} className="text-slate-400 hover:text-slate-600 transition-colors">
            <HiX className="text-xl" />
          </button>
        </div>
        <div className="p-6">{children}</div>
      </div>
    </div>
  )
}
