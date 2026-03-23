import { AuthProvider } from './context/AuthContext'
import { NotificationProvider } from './context/NotificationContext'
import { TreeProvider } from './context/TreeContext'
import AppRouter from './routes/AppRouter'
import ToastContainer from './components/common/Toast'

export default function App() {
  return (
    <AuthProvider>
      <NotificationProvider>
        <TreeProvider>
          <AppRouter />
          <ToastContainer />
        </TreeProvider>
      </NotificationProvider>
    </AuthProvider>
  )
}