import Modal from './Modal'
import { HiExclamation } from 'react-icons/hi'

export default function ConfirmDialog({ open, onClose, onConfirm, title, message }) {
  return (
    <Modal open={open} onClose={onClose} title={title} size="sm">
      <div className="flex items-start gap-3 mb-5">
        <div className="w-10 h-10 rounded-full bg-red-100 flex items-center justify-center flex-shrink-0">
          <HiExclamation className="text-red-500 text-xl" />
        </div>
        <p className="text-sm text-slate-600 pt-2">{message}</p>
      </div>
      <div className="flex gap-2 justify-end">
        <button onClick={onClose} className="btn-secondary">Cancel</button>
        <button onClick={() => { onConfirm(); onClose() }} className="btn-danger">Delete</button>
      </div>
    </Modal>
  )
}
