import { useState, useEffect } from 'react'
import Layout from '../../components/common/Layout'
import taskApi from '../../api/taskApi'
import CalendarGrid from '../../components/calendar/CalendarGrid'
import { HiChevronLeft, HiChevronRight } from 'react-icons/hi'
import dayjs from 'dayjs'

export default function CalendarPage() {
  const [currentDate, setCurrentDate] = useState(dayjs())
  const [tasks, setTasks] = useState([])

  const fetchTasks = async (date) => {
    const start = date.startOf('month').format('YYYY-MM-DD')
    const end = date.endOf('month').format('YYYY-MM-DD')
    try {
      const res = await taskApi.getMyTasks(start, end)
      setTasks(res.result || [])
    } catch (e) {
      console.error('Calendar error:', e)
    }
  }

  useEffect(() => { fetchTasks(currentDate) }, [currentDate])

  const prevMonth = () => setCurrentDate(d => d.subtract(1, 'month'))
  const nextMonth = () => setCurrentDate(d => d.add(1, 'month'))

  return (
    <Layout>
      <div className="fade-in">
        <h1 className="font-display font-bold text-3xl text-primary mb-6">Calendar</h1>
        <div className="card p-6">
          <div className="flex items-center justify-between mb-6">
            <button onClick={prevMonth}
              className="text-slate-400 hover:text-primary p-1 rounded-lg hover:bg-surface transition-colors">
              <HiChevronLeft className="text-xl" />
            </button>
            <h2 className="font-display font-semibold text-lg text-primary">
              {currentDate.format('MMMM, YYYY')}
            </h2>
            <button onClick={nextMonth}
              className="text-slate-400 hover:text-primary p-1 rounded-lg hover:bg-surface transition-colors">
              <HiChevronRight className="text-xl" />
            </button>
          </div>
          <CalendarGrid currentDate={currentDate} tasks={tasks} />
        </div>
      </div>
    </Layout>
  )
}