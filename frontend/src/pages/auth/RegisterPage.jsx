import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import authApi from '../../api/authApi'
import userApi from '../../api/userApi'
import { HiCheckCircle } from 'react-icons/hi'

export default function RegisterPage() {
  const [form, setForm] = useState({
    fullName: '', username: '', password: '', dob: '', email: '', phoneNumber: ''
  })
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    setError('')
    try {
      await userApi.register(form)
      navigate('/login')
    } catch (err) {
      setError(err?.message || 'Registration failed')
    } finally {
      setLoading(false)
    }
  }

  const fields = [
    { key: 'fullName', label: 'Fullname', type: 'text', placeholder: 'John Smith' },
    { key: 'username', label: 'Username', type: 'text', placeholder: 'johnsmith' },
    { key: 'password', label: 'Password', type: 'password', placeholder: '••••••••' },
    { key: 'dob', label: 'Date of birth', type: 'date' },
    { key: 'email', label: 'Email', type: 'email', placeholder: 'john@example.com' },
    { key: 'phoneNumber', label: 'Phone number', type: 'tel', placeholder: '0123456789' },
  ]

  return (
    <div className="min-h-screen bg-surface flex items-center justify-center p-4">
      <div className="w-full max-w-sm">
        <div className="flex items-center gap-2 mb-8">
          <HiCheckCircle className="text-primary text-3xl" />
          <span className="font-display font-bold text-2xl text-primary">Taskei</span>
        </div>

        <div className="card p-8">
          <h1 className="font-display font-bold text-2xl text-primary mb-6 text-center">Sign up</h1>

          {error && (
            <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-600 text-sm">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-3">
            {fields.map(f => (
              <div key={f.key} className="flex items-center gap-3">
                <label className="text-sm text-primary w-28 text-right flex-shrink-0">{f.label}</label>
                <input
                  className="input flex-1"
                  type={f.type}
                  placeholder={f.placeholder}
                  value={form[f.key]}
                  onChange={e => setForm(p => ({ ...p, [f.key]: e.target.value }))}
                  required
                />
              </div>
            ))}

            <div className="flex justify-end pt-3">
              <button type="submit" disabled={loading} className="btn-primary px-8">
                {loading ? 'Creating...' : 'Sign up'}
              </button>
            </div>
          </form>

          <p className="text-center text-sm text-slate-500 mt-6">
            Already have an account?{' '}
            <Link to="/login" className="text-primary font-medium hover:underline">Sign in</Link>
          </p>
        </div>
      </div>
    </div>
  )
}