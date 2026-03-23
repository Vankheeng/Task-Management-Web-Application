import { useState } from 'react'
import userApi from '../../api/userApi'
import teamMemberApi from '../../api/teamMemberApi'
import { HiSearch, HiUserAdd } from 'react-icons/hi'

export default function AddMemberForm({ teamId, onAdded }) {
  const [username, setUsername] = useState('')
  const [result, setResult] = useState(null)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSearch = async () => {
    if (!username.trim()) return
    setError('')
    setResult(null)
    try {
      const res = await userApi.searchByUsername(username.trim())
      setResult(res.result)
    } catch {
      setError('User not found')
    }
  }

  const handleAdd = async () => {
    if (!result) return
    setLoading(true)
    try {
      await teamMemberApi.addMember({ userId: result.id, teamId, role: 'MEMBER' })
      setUsername('')
      setResult(null)
      onAdded?.()
    } catch (e) {
      setError(e?.message || 'Failed to add member')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="border-b border-border">
      {/* Search row */}
      <div className="flex items-center gap-3 p-4">
        <input
          className="input flex-1"
          placeholder="Enter username to search"
          value={username}
          onChange={e => setUsername(e.target.value)}
          onKeyDown={e => e.key === 'Enter' && handleSearch()}
        />
        <button onClick={handleSearch} className="btn-secondary">
          <HiSearch /> Search
        </button>
      </div>

      {/* Error */}
      {error && (
        <p className="px-4 pb-3 text-sm text-red-500">{error}</p>
      )}

      {/* Result */}
      {result && (
        <div className="mx-4 mb-4 p-3 bg-primary-light border border-primary/20 rounded-lg flex items-center justify-between">
          <div>
            <p className="font-medium text-sm text-slate-800">{result.fullName}</p>
            <p className="text-xs text-slate-500">@{result.username} · {result.email}</p>
          </div>
          <button
            onClick={handleAdd}
            disabled={loading}
            className="btn-primary text-xs py-1 px-3"
          >
            <HiUserAdd className="text-xs" />
            {loading ? 'Adding...' : 'Add to team'}
          </button>
        </div>
      )}
    </div>
  )
}