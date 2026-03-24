import { useState } from 'react'
import Layout from '../../components/common/Layout'
import { useAuth } from '../../context/AuthContext'
import userApi from '../../api/userApi'
import { HiPencil, HiSave, HiX } from 'react-icons/hi'
import dayjs from 'dayjs'

export default function ProfilePage() {
  const { user, updateUser } = useAuth()
  const [editing, setEditing] = useState(false)
  const [form, setForm] = useState({})
  const [success, setSuccess] = useState(false)
  const [error, setError] = useState('')

  const startEdit = () => {
    setForm({
      fullName: user?.fullName || '',
      dob: user?.dob || '',
      email: user?.email || '',
      phoneNumber: user?.phoneNumber || '',
    })
    setEditing(true)
    setSuccess(false)
    setError('')
  }

  const handleSave = async () => {
    try {
      const res = await userApi.updateProfile(form)
      updateUser(res.result)
      setEditing(false)
      setSuccess(true)
    } catch (err) {
      setError(err?.message || 'Update failed')
    }
  }

  return (
    <Layout>
      <div className="fade-in max-w-2xl mx-auto">
        <h1 className="font-display font-bold text-3xl text-primary mb-6">Profile</h1>

        <div className="card p-8">
          {success && (
            <div className="mb-4 p-3 bg-green-50 border border-green-200 rounded-lg text-green-700 text-sm">
              Profile updated successfully!
            </div>
          )}
          {error && (
            <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-600 text-sm">
              {error}
            </div>
          )}

          <p className="font-display font-bold text-lg text-slate-700 mb-6">{user?.username}</p>

          <div className="space-y-4">
            {editing ? (
              <>
                {[
                  { key: 'fullName', label: 'Fullname', type: 'text' },
                  { key: 'dob', label: 'Date of birth', type: 'date' },
                  { key: 'email', label: 'Email', type: 'email' },
                  { key: 'phoneNumber', label: 'Phone number', type: 'tel' },
                ].map(f => (
                  <div key={f.key} className="flex items-center gap-3">
                    <label className="text-sm text-slate-500 w-32">{f.label}:</label>
                    <input
                      className="input flex-1"
                      type={f.type}
                      value={form[f.key]}
                      onChange={e => setForm(p => ({ ...p, [f.key]: e.target.value }))}
                    />
                  </div>
                ))}

                <div className="flex gap-3 pt-4">
                  <button onClick={handleSave} className="btn-primary">
                    <HiSave /> Save
                  </button>
                  <button onClick={() => setEditing(false)} className="btn-secondary">
                    <HiX /> Cancel
                  </button>
                </div>
              </>
            ) : (
              <>
                {[
                  { label: 'Fullname', value: user?.fullName },
                  { label: 'Date of birth', value: user?.dob ? dayjs(user.dob).format('D/M/YYYY') : '—' },
                  { label: 'Email', value: user?.email },
                  { label: 'Phone number', value: user?.phoneNumber },
                ].map(f => (
                  <div key={f.label} className="flex items-center gap-3">
                    <span className="text-sm text-slate-500 w-32">{f.label}:</span>
                    <span className="text-sm font-medium text-primary">{f.value || '—'}</span>
                  </div>
                ))}

                <div className="pt-4">
                  <button onClick={startEdit} className="btn-primary">
                    <HiPencil /> Update
                  </button>
                </div>
              </>
            )}
          </div>
        </div>
      </div>
    </Layout>
  )
}