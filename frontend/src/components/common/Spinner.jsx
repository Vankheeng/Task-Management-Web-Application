export default function Spinner({ size = 'md' }) {
  const s = { sm: 'w-4 h-4', md: 'w-6 h-6', lg: 'w-10 h-10' }[size]
  return (
    <div className={`${s} border-2 border-primary/20 border-t-primary rounded-full animate-spin`} />
  )
}

export function PageSpinner() {
  return (
    <div className="flex items-center justify-center min-h-64">
      <Spinner size="lg" />
    </div>
  )
}
