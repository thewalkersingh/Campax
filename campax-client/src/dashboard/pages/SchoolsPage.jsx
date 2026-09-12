import {useCallback, useEffect, useState} from "react";
import {listSchools, deactivateSchool, reactivateSchool} from "../lib/schoolApi";
import {ApiError} from "../lib/apiClient";
import StatusBadge from "../components/StatusBadge";
import SchoolFormModal from "../components/SchoolFormModal";
import ConfirmDialog from "../components/ConfirmDialog";

const PAGE_SIZE = 10;

export default function SchoolsPage() {
   const [schools, setSchools] = useState([]);
   const [page, setPage] = useState(0);
   const [totalPages, setTotalPages] = useState(0);
   const [search, setSearch] = useState("");
   const [searchInput, setSearchInput] = useState("");
   const [loading, setLoading] = useState(true);
   const [error, setError] = useState("");

   const [formOpen, setFormOpen] = useState(false);
   const [editingSchool, setEditingSchool] = useState(null);
   const [confirmTarget, setConfirmTarget] = useState(null); // { school, action }

   const fetchSchools = useCallback(async () => {
      setLoading(true);
      setError("");
      try {
         const result = await listSchools({search, page, size: PAGE_SIZE});
         setSchools(result.content);
         setTotalPages(result.totalPages);
      } catch (err) {
         setError(err instanceof ApiError ? err.message : "Failed to load schools.");
      } finally {
         setLoading(false);
      }
   }, [search, page]);

   useEffect(() => {
      fetchSchools();
   }, [fetchSchools]);

   // Debounce the search box so we don't fire a request on every keystroke.
   useEffect(() => {
      const timeout = setTimeout(() => {
         setPage(0);
         setSearch(searchInput);
      }, 350);
      return () => clearTimeout(timeout);
   }, [searchInput]);

   function openCreate() {
      setEditingSchool(null);
      setFormOpen(true);
   }

   function openEdit(school) {
      setEditingSchool(school);
      setFormOpen(true);
   }

   function handleSaved() {
      setFormOpen(false);
      fetchSchools();
   }

   async function handleConfirmToggle() {
      if (!confirmTarget) return;
      const {school, action} = confirmTarget;
      try {
         if (action === "deactivate") {
            await deactivateSchool(school.id);
         } else {
            await reactivateSchool(school.id);
         }
         setConfirmTarget(null);
         fetchSchools();
      } catch (err) {
         setConfirmTarget(null);
         setError(err instanceof ApiError ? err.message : "Action failed.");
      }
   }

   return (
       <div>
          <div className="flex items-center justify-between mb-6">
             <div>
                <h2 className="text-xl font-semibold text-slate-900">Schools</h2>
                <p className="text-sm text-slate-500 mt-0.5">Manage every school on the platform.</p>
             </div>
             <button
                 type="button"
                 onClick={openCreate}
                 className="px-4 py-2 text-sm font-medium rounded-md bg-indigo-600 text-white hover:bg-indigo-700"
             >
                + Add School
             </button>
          </div>

          <div className="mb-4">
             <input
                 type="text"
                 value={searchInput}
                 onChange={(e) => setSearchInput(e.target.value)}
                 placeholder="Search by name..."
                 className="w-full max-w-xs rounded-md border border-slate-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
             />
          </div>

          {error && (
              <div className="mb-4 rounded-md bg-red-50 border border-red-200 text-red-700 text-sm px-3 py-2">
                 {error}
              </div>
          )}

          <div className="bg-white rounded-lg border border-slate-200 overflow-hidden">
             <table className="w-full text-sm">
                <thead className="bg-slate-50 border-b border-slate-200">
                <tr className="text-left text-slate-500">
                   <th className="px-4 py-3 font-medium">Name</th>
                   <th className="px-4 py-3 font-medium">Subdomain</th>
                   <th className="px-4 py-3 font-medium">Plan</th>
                   <th className="px-4 py-3 font-medium">Status</th>
                   <th className="px-4 py-3 font-medium text-right">Actions</th>
                </tr>
                </thead>
                <tbody className="divide-y divide-slate-100">
                {loading ? (
                    <tr>
                       <td colSpan={5} className="px-4 py-8 text-center text-slate-400">
                          Loading...
                       </td>
                    </tr>
                ) : schools.length === 0 ? (
                    <tr>
                       <td colSpan={5} className="px-4 py-8 text-center text-slate-400">
                          No schools found.
                       </td>
                    </tr>
                ) : (
                    schools.map((school) => (
                        <tr key={school.id} className="hover:bg-slate-50">
                           <td className="px-4 py-3 font-medium text-slate-900">{school.name}</td>
                           <td className="px-4 py-3 text-slate-500">{school.subdomain}</td>
                           <td className="px-4 py-3 text-slate-500 capitalize">{school.planTier}</td>
                           <td className="px-4 py-3">
                              <StatusBadge active={school.active}/>
                           </td>
                           <td className="px-4 py-3">
                              <div className="flex justify-end gap-3">
                                 <button
                                     type="button"
                                     onClick={() => openEdit(school)}
                                     className="text-indigo-600 hover:text-indigo-800 font-medium"
                                 >
                                    Edit
                                 </button>
                                 {school.active ? (
                                     <button
                                         type="button"
                                         onClick={() => setConfirmTarget({school, action: "deactivate"})}
                                         className="text-red-600 hover:text-red-800 font-medium"
                                     >
                                        Deactivate
                                     </button>
                                 ) : (
                                     <button
                                         type="button"
                                         onClick={() => setConfirmTarget({school, action: "reactivate"})}
                                         className="text-green-600 hover:text-green-800 font-medium"
                                     >
                                        Reactivate
                                     </button>
                                 )}
                              </div>
                           </td>
                        </tr>
                    ))
                )}
                </tbody>
             </table>
          </div>

          {totalPages > 1 && (
              <div className="flex items-center justify-between mt-4">
                 <button
                     type="button"
                     onClick={() => setPage((p) => Math.max(0, p - 1))}
                     disabled={page === 0}
                     className="px-3 py-1.5 text-sm font-medium rounded-md border border-slate-300 text-slate-700 disabled:opacity-40"
                 >
                    Previous
                 </button>
                 <span className="text-sm text-slate-500">
            Page {page + 1} of {totalPages}
          </span>
                 <button
                     type="button"
                     onClick={() => setPage((p) => Math.min(totalPages - 1, p + 1))}
                     disabled={page >= totalPages - 1}
                     className="px-3 py-1.5 text-sm font-medium rounded-md border border-slate-300 text-slate-700 disabled:opacity-40"
                 >
                    Next
                 </button>
              </div>
          )}

          <SchoolFormModal
              open={formOpen}
              school={editingSchool}
              onClose={() => setFormOpen(false)}
              onSaved={handleSaved}
          />

          <ConfirmDialog
              open={Boolean(confirmTarget)}
              title={confirmTarget?.action === "deactivate" ? "Deactivate school?" : "Reactivate school?"}
              message={
                 confirmTarget?.action === "deactivate"
                     ? `"${confirmTarget?.school.name}" will be marked inactive. Historical data is preserved and this can be reversed.`
                     : `"${confirmTarget?.school.name}" will be marked active again.`
              }
              confirmLabel={confirmTarget?.action === "deactivate" ? "Deactivate" : "Reactivate"}
              danger={confirmTarget?.action === "deactivate"}
              onConfirm={handleConfirmToggle}
              onCancel={() => setConfirmTarget(null)}
          />
       </div>
   );
}