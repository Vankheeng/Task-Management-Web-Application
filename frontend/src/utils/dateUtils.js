import dayjs from 'dayjs'

export const formatDate = (date) => date ? dayjs(date).format('D/M/YYYY') : '—'
export const formatDateTime = (date) => date ? dayjs(date).format('D/M/YYYY HH:mm') : '—'
export const isOverdue = (date) => date ? dayjs(date).isBefore(dayjs(), 'day') : false
export const today = () => dayjs().format('YYYY-MM-DD')
