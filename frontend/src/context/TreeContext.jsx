import { createContext, useContext, useState, useCallback } from 'react'

export const TreeContext = createContext(null)

export function TreeProvider({ children }) {
  const [version, setVersion] = useState(0)
  const refreshTree = useCallback(() => setVersion(v => v + 1), [])
  return (
    <TreeContext.Provider value={{ version, refreshTree }}>
      {children}
    </TreeContext.Provider>
  )
}

export const useTree = () => useContext(TreeContext)