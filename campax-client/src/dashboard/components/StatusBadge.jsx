// src/dashboard/components/StatusBadge.jsx
export default function StatusBadge({ active }) {
  return (
    <span
      className={`inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium ${
        active ? "bg-green-100 text-green-700" : "bg-slate-100 text-slate-500"
      }`}
    >
      {active ? "Active" : "Inactive"}
    </span>
  );
}
