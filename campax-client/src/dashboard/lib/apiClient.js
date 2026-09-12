// src/dashboard/lib/apiClient.js
const BASE_URL = "http://localhost:8080";

/**
 * Thrown for any non-2xx response. `details.fieldErrors` is populated
 * when the backend's ProblemDetail response includes a field->message
 * map (bean validation failures); `details.generalErrors` is populated
 * when it includes a plain list (custom field validation failures).
 * `message` is always safe to show as a one-line fallback.
 */
export class ApiError extends Error {
  constructor(message, status, details) {
    super(message);
    this.name = "ApiError";
    this.status = status;
    this.details = details;
  }
}

async function request(path, options = {}) {
  const response = await fetch(`${BASE_URL}${path}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...options.headers,
    },
  });

  if (response.status === 204) {
    return null;
  }

  const contentType = response.headers.get("content-type") || "";
  const body = contentType.includes("json") ? await response.json() : null;

  if (!response.ok) {
    const message =
      body?.detail || body?.title || `Request failed with status ${response.status}`;
    const fieldErrors =
      body?.errors && !Array.isArray(body.errors) ? body.errors : undefined;
    const generalErrors = Array.isArray(body?.errors) ? body.errors : undefined;
    throw new ApiError(message, response.status, { fieldErrors, generalErrors });
  }

  return body;
}

export const apiClient = {
  get: (path) => request(path, { method: "GET" }),
  post: (path, data) => request(path, { method: "POST", body: JSON.stringify(data) }),
  patch: (path, data) => request(path, { method: "PATCH", body: JSON.stringify(data) }),
  delete: (path) => request(path, { method: "DELETE" }),
};
