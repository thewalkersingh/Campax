import { apiClient } from './apiClient';

const buildQueryString = (params) => {
   if (!params) return '';
   const query = new URLSearchParams();
   Object.entries(params).forEach(([key, value]) => {
      if (value !== undefined && value !== null && value !== '') {
         query.append(key, value);
      }
   });
   const qs = query.toString();
   return qs ? `?${qs}` : '';
};

export const studentApi = {
   getList: (params) => apiClient.get(`/api/v1/students${buildQueryString(params)}`),
   getById: (id) => apiClient.get(`/api/v1/students/${id}`),
   create: (data) => apiClient.post('/api/v1/students', data),
   update: (id, data) => apiClient.patch(`/api/v1/students/${id}`, data),
   deactivate: (id) => apiClient.delete(`/api/v1/students/${id}`),
   reactivate: (id) => apiClient.post(`/api/v1/students/${id}/reactivate`),
   addGuardian: (studentId, data) => apiClient.post(`/api/v1/students/${studentId}/guardians`, data),
   removeGuardian: (studentId, guardianId) => apiClient.delete(`/api/v1/students/${studentId}/guardians/${guardianId}`),
};

export const platformApi = {
   getSchools: (params) => apiClient.get(`/api/v1/platform/schools${buildQueryString(params)}`),
};