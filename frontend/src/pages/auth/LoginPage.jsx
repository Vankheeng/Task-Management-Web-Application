import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'
import { HiCheckCircle, HiEye, HiEyeOff } from 'react-icons/hi'

export default function LoginPage() {
  const [form, setForm] = useState({ username: '', password: '' })
  const [showPw, setShowPw] = useState(false)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const { login } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setLoading(true)
    setError('')
    try {
      await login(form.username, form.password)
      navigate('/')
    } catch (err) {
      setError(err?.message || 'Invalid username or password')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="min-h-screen bg-surface flex items-center justify-center p-4">
      <div className="w-full max-w-sm">
        {/* Logo */}
        <div className="flex items-center gap-2 mb-8">
          <HiCheckCircle className="text-primary text-3xl" />
          <span className="font-display font-bold text-2xl text-primary">Taskei</span>
        </div>

        <div className="card p-8">
          <h1 className="font-display font-bold text-2xl text-primary mb-6 text-center">Sign In</h1>

          {error && (
            <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg text-red-600 text-sm">
              {error}
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="flex items-center gap-3">
              <label className="text-sm text-slate-600 w-24 text-right flex-shrink-0">Username:</label>
              <input
                className="input flex-1"
                placeholder="Enter username"
                value={form.username}
                onChange={e => setForm(p => ({ ...p, username: e.target.value }))}
                required
              />
            </div>

            <div className="flex items-center gap-3">
              <label className="text-sm text-slate-600 w-24 text-right flex-shrink-0">Password:</label>
              <div className="relative flex-1">
                <input
                  className="input pr-10"
                  type={showPw ? 'text' : 'password'}
                  placeholder="Enter password"
                  value={form.password}
                  onChange={e => setForm(p => ({ ...p, password: e.target.value }))}
                  required
                />
                <button
                  type="button"
                  onClick={() => setShowPw(p => !p)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400"
                >
                  {showPw ? <HiEyeOff /> : <HiEye />}
                </button>
              </div>
            </div>

            <div className="flex justify-end pt-2">
              <button
                type="submit"
                disabled={loading}
                className="btn-primary px-8"
              >
                {loading ? 'Logging in...' : 'Login'}
              </button>
            </div>
          </form>

          <p className="text-center text-sm text-slate-500 mt-6">
            Don't have an account?{' '}
            <Link to="/register" className="text-primary font-medium hover:underline">
              Sign up
            </Link>
          </p>
        </div>
      </div>
    </div>
  )
}