// src/dashboard/lib/schoolApi.js
import { apiClient } from "./apiClient";

const BASE_PATH = "/api/v1/platform/schools";

export function listSchools({ search = "", page = 0, size = 10 } = {}) {
  const params = new URLSearchParams();
  if (search) params.set("search", search);
  params.set("page", String(page));
  params.set("size", String(size));
  return apiClient.get(`${BASE_PATH}?${params.toString()}`);
}

export function createSchool(data) {
  return apiClient.post(BASE_PATH, data);
}

export function updateSchool(id, data) {
  return apiClient.patch(`${BASE_PATH}/${id}`, data);
}

export function deactivateSchool(id) {
  return apiClient.delete(`${BASE_PATH}/${id}`);
}

export function reactivateSchool(id) {
  return apiClient.post(`${BASE_PATH}/${id}/reactivate`);
}
