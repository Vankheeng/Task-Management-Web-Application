import { useState } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'
import { useNotification } from '../../context/NotificationContext'
import {
  HiOutlineUsers, HiOutlineCalendar, HiOutlineBell, HiOutlineLogout,
  HiOutlineUser, HiCheckCircle, HiChevronRight, HiChevronDown,
  HiChevronLeft
} from 'react-icons/hi'

export default function Sidebar({ treeData = {}, selectedTeamName = '', selectedTeamId = '', collapsed = false, onCollapse }) {
  const navigate = useNavigate()
  const location = useLocation()
  const { user, logout } = useAuth()
  const { unreadCount } = useNotification()
  const setCollapsed = onCollapse || (() => {})

  const handleLogout = async () => {
    await logout()
    navigate('/login')
  }

  const handleClickTeams = () => {
    localStorage.removeItem('selectedTeamId')
    localStorage.removeItem('selectedTeamName')
    localStorage.removeItem('selectedProjectId')
    localStorage.removeItem('selectedTeamRole')
    navigate('/')
  }

  const hasTree = treeData.projects && treeData.projects.length > 0

  return (
    <aside className={`${collapsed ? 'w-16' : 'w-60'} min-h-screen bg-white border-r border-border flex flex-col fixed left-0 top-0 z-20 transition-all duration-200`}>
      {/* Logo — luôn hiện icon + text Taskei */}
      <div className="border-b border-border flex items-center justify-between px-3 py-4">
        {collapsed ? (
          // Collapsed: chỉ icon giữa
          <div className="flex flex-col items-center w-full gap-2">
            <button onClick={() => navigate('/')} className="text-primary" title="Taskei">
              <HiCheckCircle className="text-2xl" />
            </button>
            <button onClick={() => setCollapsed(false)}
              className="text-slate-400 hover:text-primary p-1 rounded-lg hover:bg-surface"
              title="Expand">
              <HiChevronRight className="text-base" />
            </button>
          </div>
        ) : (
          // Expanded: logo + collapse button
          <>
            <button onClick={() => navigate('/')} className="flex items-center gap-2">
              <HiCheckCircle className="text-primary text-2xl flex-shrink-0" />
              <span className="font-display font-bold text-xl text-primary">Taskei</span>
            </button>
            <button onClick={() => setCollapsed(true)}
              className="text-slate-400 hover:text-primary p-1 rounded-lg hover:bg-surface flex-shrink-0"
              title="Collapse">
              <HiChevronLeft className="text-base" />
            </button>
          </>
        )}
      </div>

      {/* Nav */}
      <nav className="flex-1 p-2 overflow-y-auto">
        <SidebarBtn
          icon={<HiOutlineUsers />}
          label="Teams"
          active={location.pathname === '/'}
          collapsed={collapsed}
          onClick={handleClickTeams}
        />

        {/* Team name */}
        {!collapsed && selectedTeamName && selectedTeamId && (
          <button
            onClick={() => navigate(`/teams/${selectedTeamId}`)}
            className="w-full px-3 py-1.5 mt-1 mb-1 text-left hover:bg-primary-light rounded-lg transition-colors"
          >
            <span className="text-xs font-bold text-primary uppercase tracking-wide truncate block">
              {selectedTeamName}
            </span>
          </button>
        )}

        {/* Tree */}
        {!collapsed && hasTree && (
          <div className="slide-in">
            <TeamTree data={treeData} navigate={navigate} location={location} />
          </div>
        )}
      </nav>

      {/* Bottom */}
      <div className="p-2 border-t border-border space-y-1">
        <SidebarBtn
          icon={<HiOutlineCalendar />}
          label="Calendar"
          active={location.pathname === '/calendar'}
          collapsed={collapsed}
          onClick={() => navigate('/calendar')}
        />
        <SidebarBtn
          icon={<HiOutlineBell />}
          label="Notification"
          active={location.pathname === '/notifications'}
          collapsed={collapsed}
          onClick={() => navigate('/notifications')}
          badge={unreadCount > 0 ? <span className="ml-auto badge bg-red-100 text-red-600 text-xs">{unreadCount}</span> : null}
        />
        <SidebarBtn
          icon={<HiOutlineUser />}
          label={user?.fullName || user?.username || ''}
          active={location.pathname === '/profile'}
          collapsed={collapsed}
          onClick={() => navigate('/profile')}
          suffix={
            !collapsed && (
              <HiOutlineLogout
                className="text-slate-400 hover:text-red-500 transition-colors flex-shrink-0"
                onClick={e => { e.stopPropagation(); handleLogout() }}
              />
            )
          }
        />
      </div>
    </aside>
  )
}

function SidebarBtn({ icon, label, active, collapsed, onClick, badge, suffix }) {
  return (
    <button
      onClick={onClick}
      title={collapsed ? label : undefined}
      className={`flex items-center gap-3 w-full px-3 py-2 rounded-lg text-sm font-medium transition-colors
        text-slate-600 hover:bg-primary-light hover:text-primary
        ${active ? 'bg-primary-light text-primary' : ''}
        ${collapsed ? 'justify-center' : ''}`}
    >
      <span className="text-lg flex-shrink-0">{icon}</span>
      {!collapsed && <span className="flex-1 text-left truncate">{label}</span>}
      {!collapsed && badge}
      {suffix}
    </button>
  )
}

function TeamTree({ data, navigate, location }) {
  return (
    <div className="space-y-0.5">
      {(data.projects || []).map(project => (
        <ProjectNode key={project.id} project={project} navigate={navigate} location={location} />
      ))}
    </div>
  )
}

function ProjectNode({ project, navigate, location }) {
  const isActive = location.pathname === `/projects/${project.id}`
  const [expanded, setExpanded] = useState(false)
  const hasList = project.taskLists && project.taskLists.length > 0

  return (
    <div>
      <div className={`flex items-center px-3 py-2 rounded-lg text-sm font-medium transition-colors
        text-slate-600 hover:bg-primary-light hover:text-primary cursor-pointer
        ${isActive ? 'bg-primary-light text-primary' : ''}`}>
        <button onClick={() => setExpanded(e => !e)} className="mr-1 text-slate-400 hover:text-primary flex-shrink-0 w-4">
          {expanded ? <HiChevronDown className="text-xs" /> : <HiChevronRight className="text-xs" />}
        </button>
        <button
          onClick={() => { localStorage.setItem('selectedProjectId', project.id); navigate(`/projects/${project.id}`); setExpanded(true) }}
          className="flex-1 text-left truncate text-xs">
          {project.name}
        </button>
      </div>
      {expanded && hasList && (
        <div className="slide-in">
          {project.taskLists.map(tl => (
            <TaskListNode key={tl.id} taskList={tl} projectId={project.id} navigate={navigate} location={location} />
          ))}
        </div>
      )}
    </div>
  )
}

function TaskListNode({ taskList, projectId, navigate, location }) {
  const isActive = location.pathname === `/task-lists/${taskList.id}`
  const [expanded, setExpanded] = useState(false)
  const hasTasks = taskList.tasks && taskList.tasks.length > 0

  return (
    <div>
      <div className={`flex items-center pl-7 pr-3 py-2 rounded-lg text-sm font-medium transition-colors
        text-slate-600 hover:bg-primary-light hover:text-primary cursor-pointer
        ${isActive ? 'bg-primary-light text-primary' : ''}`}>
        <button onClick={() => setExpanded(e => !e)} className="mr-1 text-slate-400 hover:text-primary flex-shrink-0 w-4">
          {expanded ? <HiChevronDown className="text-xs" /> : <HiChevronRight className="text-xs" />}
        </button>
        <button
          onClick={() => { localStorage.setItem('selectedProjectId', projectId); navigate(`/task-lists/${taskList.id}`); setExpanded(true) }}
          className="flex-1 text-left truncate text-xs">
          {taskList.name}
        </button>
      </div>
      {expanded && hasTasks && (
        <div className="slide-in">
          {taskList.tasks.map(task => (
            <button key={task.id} onClick={() => navigate(`/tasks/${task.id}`)}
              className={`flex items-center w-full text-left pl-12 pr-3 py-1.5 rounded-lg text-xs font-medium transition-colors
                text-slate-600 hover:bg-primary-light hover:text-primary
                ${location.pathname === `/tasks/${task.id}` ? 'bg-primary-light text-primary' : ''}`}>
              <span className="truncate">{task.title}</span>
            </button>
          ))}
        </div>
      )}
    </div>
  )
}