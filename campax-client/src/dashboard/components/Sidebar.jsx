// src/dashboard/components/Sidebar.jsx
const navItems = [
  { label: "Schools", active: true },
  { label: "Students", active: false },
  { label: "Staff", active: false },
  { label: "Settings", active: false },
];

export default function Sidebar() {
  return (
    <aside className="w-64 shrink-0 bg-slate-900 text-slate-100 flex flex-col">
      <div className="px-6 py-5 border-b border-slate-800">
        <h1 className="text-lg font-semibold tracking-tight">Campax Admin</h1>
        <p className="text-xs text-slate-400 mt-0.5">Platform Dashboard</p>
      </div>

      <nav className="flex-1 px-3 py-4 space-y-1">
        {navItems.map((item) => (
          <div
            key={item.label}
            className={`px-3 py-2 rounded-md text-sm font-medium flex items-center justify-between ${
              item.active
                ? "bg-indigo-600 text-white"
                : "text-slate-400 cursor-not-allowed"
            }`}
          >
            <span>{item.label}</span>
            {!item.active && (
              <span className="text-[10px] uppercase tracking-wide bg-slate-800 text-slate-500 px-1.5 py-0.5 rounded">
                Soon
              </span>
            )}
          </div>
        ))}
      </nav>

      <div className="px-6 py-4 border-t border-slate-800 text-xs text-slate-500">
        No auth yet - every request is unauthenticated.
      </div>
    </aside>
  );
}
