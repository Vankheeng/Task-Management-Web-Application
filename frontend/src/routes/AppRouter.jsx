import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from '../pages/auth/LoginPage';
import RegisterPage from '../pages/auth/RegisterPage';
import TeamsPage from '../pages/team/TeamsPage';
import ProjectDetailPage from '../pages/project/ProjectDetailPage';
import TaskListDetailPage from '../pages/taskList/TaskListDetailPage';
import TaskDetailPage from '../pages/task/TaskDetailPage';
import NotificationPage from '../pages/notification/NotificationPage';
import CalendarPage from '../pages/calendar/CalendarPage';
import ProfilePage from '../pages/profile/ProfilePage';
import TeamDetailPage from '../pages/team/TeamDetailPage';

const PrivateRoute = ({ children }) => {
    const token = localStorage.getItem('token');
    return token ? children : <Navigate to="/login" />;
};

export default function AppRouter() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />
                <Route path="/" element={<PrivateRoute><TeamsPage /></PrivateRoute>} />
                <Route path="/teams/:teamId" element={<PrivateRoute><TeamDetailPage /></PrivateRoute>} />
                <Route path="/projects/:projectId" element={<PrivateRoute><ProjectDetailPage /></PrivateRoute>} />
                <Route path="/task-lists/:taskListId" element={<PrivateRoute><TaskListDetailPage /></PrivateRoute>} />
                <Route path="/tasks/:taskId" element={<PrivateRoute><TaskDetailPage /></PrivateRoute>} />
                <Route path="/notifications" element={<PrivateRoute><NotificationPage /></PrivateRoute>} />
                <Route path="/calendar" element={<PrivateRoute><CalendarPage /></PrivateRoute>} />
                <Route path="/profile" element={<PrivateRoute><ProfilePage /></PrivateRoute>} />
            </Routes>
        </BrowserRouter>
    );
}