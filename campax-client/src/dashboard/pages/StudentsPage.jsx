import {useState, useEffect} from 'react';
import {studentApi, platformApi} from '../lib/studentApi';
import StudentFormModal from '../components/StudentFormModal';
import ConfirmDialog from '../components/ConfirmDialog';
import StatusBadge from '../components/StatusBadge';

export default function StudentsPage() {
   const [schools, setSchools] = useState([]);
   const [selectedSchoolId, setSelectedSchoolId] = useState('');
   const [search, setSearch] = useState('');
   const [debouncedSearch, setDebouncedSearch] = useState('');
   const [page, setPage] = useState(0);
   const [size] = useState(20);

   const [students, setStudents] = useState([]);
   const [totalElements, setTotalElements] = useState(0);
   const [totalPages, setTotalPages] = useState(0);
   const [loading, setLoading] = useState(false);
   const [error, setError] = useState('');
   const [refresh, setRefresh] = useState(0);

   const [isFormOpen, setIsFormOpen] = useState(false);
   const [editingStudent, setEditingStudent] = useState(null);
   const [confirmAction, setConfirmAction] = useState(null);

   // Load schools on mount
   useEffect(() => {
      const loadSchools = async () => {
         try {
            const res = await platformApi.getSchools({page: 0, size: 100});
            const schoolList = res.content || [];
            setSchools(schoolList);
            if (schoolList.length > 0 && !selectedSchoolId) {
               setSelectedSchoolId(schoolList[0].id);
            }
         } catch (err) {
            setError('Failed to load schools');
         }
      };
      loadSchools();
   }, []);

   // Debounce search
   useEffect(() => {
      const timer = setTimeout(() => setDebouncedSearch(search), 350);
      return () => clearTimeout(timer);
   }, [search]);

   // Load students
   useEffect(() => {
      if (!selectedSchoolId) return;

      const loadStudents = async () => {
         setLoading(true);
         setError('');
         try {
            const res = await studentApi.getList({
               schoolId: selectedSchoolId,
               search: debouncedSearch,
               page,
               size,
            });
            setStudents(res.content || []);
            setTotalElements(res.totalElements || 0);
            setTotalPages(res.totalPages || 0);
         } catch (err) {
            setError(err.message || 'Failed to load students');
         } finally {
            setLoading(false);
         }
      };
      loadStudents();
   }, [selectedSchoolId, debouncedSearch, page, size, refresh]);

   const handleEdit = async (student) => {
      try {
         setLoading(true);
         const fullStudent = await studentApi.getById(student.id);
         setEditingStudent(fullStudent);
         setIsFormOpen(true);
      } catch (err) {
         setError(err.message || 'Failed to load student details');
      } finally {
         setLoading(false);
      }
   };

   const handleConfirmAction = async () => {
      if (!confirmAction) return;
      setLoading(true);
      try {
         if (confirmAction.type === 'deactivate') {
            await studentApi.deactivate(confirmAction.studentId);
         } else {
            await studentApi.reactivate(confirmAction.studentId);
         }
         setRefresh(r => r + 1);
      } catch (err) {
         setError(err.message || 'Action failed');
      } finally {
         setLoading(false);
         setConfirmAction(null);
      }
   };

   return (
       <div className="space-y-6">
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
             <h2 className="text-2xl font-bold text-slate-900">Students</h2>
             <button
                 onClick={() => {
                    setEditingStudent(null);
                    setIsFormOpen(true);
                 }}
                 className="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors"
             >
                Add Student
             </button>
          </div>

          {error && (
              <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-md text-sm">
                 {error}
              </div>
          )}

          <div className="flex flex-col sm:flex-row gap-4 bg-white p-4 rounded-lg border border-slate-200">
             <select
                 value={selectedSchoolId}
                 onChange={(e) => {
                    setSelectedSchoolId(e.target.value);
                    setPage(0);
                 }}
                 className="border border-slate-300 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
             >
                <option value="">Select a School</option>
                {schools.map((school) => (
                    <option key={school.id} value={school.id}>{school.name}</option>
                ))}
             </select>

             <input
                 type="text"
                 placeholder="Search by name, email, or admission no..."
                 value={search}
                 onChange={(e) => {
                    setSearch(e.target.value);
                    setPage(0);
                 }}
                 className="flex-1 border border-slate-300 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
             />
          </div>

          <div className="bg-white rounded-lg border border-slate-200 overflow-hidden">
             {loading ? (
                 <div className="p-8 text-center text-slate-500">Loading...</div>
             ) : students.length === 0 ? (
                 <div className="p-8 text-center text-slate-500">No students found.</div>
             ) : (
                 <div className="overflow-x-auto">
                    <table className="w-full text-sm text-left">
                       <thead className="bg-slate-50 text-slate-600 font-medium border-b border-slate-200">
                       <tr>
                          <th className="px-6 py-3">Name</th>
                          <th className="px-6 py-3">Admission No</th>
                          <th className="px-6 py-3">Section</th>
                          <th className="px-6 py-3">Status</th>
                          <th className="px-6 py-3 text-right">Actions</th>
                       </tr>
                       </thead>
                       <tbody className="divide-y divide-slate-100">
                       {students.map((student) => (
                           <tr key={student.id} className="hover:bg-slate-50">
                              <td className="px-6 py-4">
                                 <div className="font-medium text-slate-900">{student.fullName}</div>
                                 <div className="text-slate-500 text-xs">{student.email}</div>
                              </td>
                              <td className="px-6 py-4 text-slate-700">{student.admissionNo}</td>
                              <td className="px-6 py-4 text-slate-700">{student.sectionLabel || '-'}</td>
                              <td className="px-6 py-4">
                                 <StatusBadge active={student.active}/>
                              </td>
                              <td className="px-6 py-4 text-right space-x-3">
                                 <button
                                     onClick={() => handleEdit(student)}
                                     className="text-indigo-600 hover:text-indigo-800 text-sm font-medium"
                                 >
                                    Edit
                                 </button>
                                 {student.active ? (
                                     <button
                                         onClick={() => setConfirmAction({
                                            type: 'deactivate',
                                            studentId: student.id,
                                            studentName: student.fullName
                                         })}
                                         className="text-red-600 hover:text-red-800 text-sm font-medium"
                                     >
                                        Deactivate
                                     </button>
                                 ) : (
                                     <button
                                         onClick={() => setConfirmAction({
                                            type: 'reactivate',
                                            studentId: student.id,
                                            studentName: student.fullName
                                         })}
                                         className="text-emerald-600 hover:text-emerald-800 text-sm font-medium"
                                     >
                                        Reactivate
                                     </button>
                                 )}
                              </td>
                           </tr>
                       ))}
                       </tbody>
                    </table>
                 </div>
             )}

             {totalPages > 1 && (
                 <div className="flex items-center justify-between px-6 py-3 border-t border-slate-200 bg-slate-50">
                    <div className="text-sm text-slate-600">
                       Showing {page * size + 1} to {Math.min((page + 1) * size, totalElements)} of {totalElements} results
                    </div>
                    <div className="flex gap-2">
                       <button
                           onClick={() => setPage(p => Math.max(0, p - 1))}
                           disabled={page === 0}
                           className="px-3 py-1 border border-slate-300 rounded-md text-sm disabled:opacity-50 disabled:cursor-not-allowed hover:bg-slate-100"
                       >
                          Previous
                       </button>
                       <button
                           onClick={() => setPage(p => Math.min(totalPages - 1, p + 1))}
                           disabled={page >= totalPages - 1}
                           className="px-3 py-1 border border-slate-300 rounded-md text-sm disabled:opacity-50 disabled:cursor-not-allowed hover:bg-slate-100"
                       >
                          Next
                       </button>
                    </div>
                 </div>
             )}
          </div>

          {isFormOpen && (
              <StudentFormModal
                  isOpen={isFormOpen}
                  onClose={() => {
                     setIsFormOpen(false);
                     setEditingStudent(null);
                  }}
                  onSuccess={() => {
                     setIsFormOpen(false);
                     setEditingStudent(null);
                     setRefresh(r => r + 1);
                  }}
                  student={editingStudent}
                  schools={schools}
              />
          )}

          {confirmAction && (
              <ConfirmDialog
                  isOpen={!!confirmAction}
                  onClose={() => setConfirmAction(null)}
                  onConfirm={handleConfirmAction}
                  title={confirmAction.type === 'deactivate' ? 'Deactivate Student' : 'Reactivate Student'}
                  message={`Are you sure you want to ${confirmAction.type} ${confirmAction.studentName}?`}
                  confirmText={confirmAction.type === 'deactivate' ? 'Deactivate' : 'Reactivate'}
                  confirmVariant={confirmAction.type === 'deactivate' ? 'danger' : 'primary'}
              />
          )}
       </div>
   );
}