import { useState, useEffect } from 'react'
import { HiCheckCircle, HiXCircle, HiX } from 'react-icons/hi'

let toastFn = null

export function useToast() {
  return {
    success: (msg) => toastFn?.('success', msg),
    error: (msg) => toastFn?.('error', msg),
  }
}

export default function ToastContainer() {
  const [toasts, setToasts] = useState([])

  useEffect(() => {
    toastFn = (type, message) => {
      const id = Date.now()
      setToasts(p => [...p, { id, type, message }])
      setTimeout(() => setToasts(p => p.filter(t => t.id !== id)), 3000)
    }
    return () => { toastFn = null }
  }, [])

  return (
    <div className="fixed bottom-4 right-4 z-50 space-y-2">
      {toasts.map(t => (
        <div key={t.id}
          className={`flex items-center gap-2 px-4 py-3 rounded-xl shadow-modal text-sm font-medium
            fade-in ${t.type === 'success' ? 'bg-green-50 text-green-700 border border-green-200' : 'bg-red-50 text-red-700 border border-red-200'}`}>
          {t.type === 'success' ? <HiCheckCircle className="text-lg" /> : <HiXCircle className="text-lg" />}
          {t.message}
          <button onClick={() => setToasts(p => p.filter(x => x.id !== t.id))}
            className="ml-2 opacity-50 hover:opacity-100"><HiX /></button>
        </div>
      ))}
    </div>
  )
}
