import {useState, useEffect} from 'react';
import {studentApi} from '../lib/studentApi';
import Modal from './Modal';

export default function StudentFormModal({isOpen, onClose, onSuccess, student, schools}) {
   const isEdit = !!student;
   const [loading, setLoading] = useState(false);
   const [errors, setErrors] = useState({});

   const [formData, setFormData] = useState({
      fullName: '', email: '', phone: '', schoolId: '', admissionNo: '',
      dateOfBirth: '', gender: '', admissionDate: '', classSectionId: '', active: true
   });

   // Create mode guardian slots
   const [guardianSlots, setGuardianSlots] = useState([]);

   // Edit mode guardian management
   const [localGuardians, setLocalGuardians] = useState([]);
   const [isAddingGuardian, setIsAddingGuardian] = useState(false);
   const [guardianForm, setGuardianForm] = useState({
      mode: 'new',
      guardianId: '',
      newGuardian: {fullName: '', phone: '', email: '', occupation: ''},
      relationship: 'FATHER',
      primaryContact: false,
      canPickup: false,
      emergencyContact: false
   });

   useEffect(() => {
      if (isOpen) {
         setErrors({});
         if (isEdit) {
            setFormData({
               fullName: student.fullName || '',
               phone: student.phone || '',
               classSectionId: student.classSectionId || '',
               dateOfBirth: student.dateOfBirth || '',
               gender: student.gender || '',
               active: student.active ?? true
            });
            setLocalGuardians(student.guardians || []);
         } else {
            setFormData({
               fullName: '', email: '', phone: '', schoolId: schools[0]?.id || '', admissionNo: '',
               dateOfBirth: '', gender: '', admissionDate: '', classSectionId: '', active: true
            });
            setGuardianSlots([]);
         }
         setIsAddingGuardian(false);
      }
   }, [isOpen, student, schools, isEdit]);

   const handleInputChange = (e) => {
      const {name, value, type, checked} = e.target;
      setFormData(prev => ({...prev, [name]: type === 'checkbox' ? checked : value}));
   };

   const handleGuardianFormChange = (field, value) => {
      if (field.startsWith('newGuardian.')) {
         const nf = field.split('.')[1];
         setGuardianForm(prev => ({...prev, newGuardian: {...prev.newGuardian, [nf]: value}}));
      } else {
         setGuardianForm(prev => ({...prev, [field]: value}));
      }
   };

   const handleAddGuardianSlot = () => {
      setGuardianSlots(prev => [...prev, {
         id: Date.now(),
         mode: 'new',
         guardianId: '',
         newGuardian: {fullName: '', phone: '', email: '', occupation: ''},
         relationship: 'FATHER',
         primaryContact: false,
         canPickup: false,
         emergencyContact: false
      }]);
   };

   const handleRemoveGuardianSlot = (id) => {
      setGuardianSlots(prev => prev.filter(g => g.id !== id));
   };

   const handleGuardianSlotChange = (id, field, value) => {
      setGuardianSlots(prev => prev.map(g => {
         if (g.id === id) {
            if (field.startsWith('newGuardian.')) {
               const nf = field.split('.')[1];
               return {...g, newGuardian: {...g.newGuardian, [nf]: value}};
            }
            return {...g, [field]: value};
         }
         return g;
      }));
   };

   const handleSubmit = async (e) => {
      e.preventDefault();
      setLoading(true);
      setErrors({});

      try {
         if (isEdit) {
            await studentApi.update(student.id, formData);
            onSuccess();
         } else {
            const payload = {...formData};
            if (guardianSlots.length > 0) {
               payload.guardians = guardianSlots.map(g => {
                  const req = {
                     relationship: g.relationship,
                     primaryContact: g.primaryContact,
                     canPickup: g.canPickup,
                     emergencyContact: g.emergencyContact
                  };
                  if (g.mode === 'existing') {
                     req.guardianId = g.guardianId;
                  } else {
                     req.newGuardian = g.newGuardian;
                  }
                  return req;
               });
            }
            await studentApi.create(payload);
            onSuccess();
         }
      } catch (err) {
         if (err.details?.fieldErrors) {
            setErrors(err.details.fieldErrors);
         } else {
            setErrors({general: err.message || 'An error occurred'});
         }
      } finally {
         setLoading(false);
      }
   };

   const handleAddGuardianToExisting = async () => {
      if (guardianForm.mode === 'existing' && !guardianForm.guardianId) {
         setErrors({guardian: 'Guardian ID is required'});
         return;
      }
      if (guardianForm.mode === 'new' && !guardianForm.newGuardian.fullName) {
         setErrors({guardian: 'Full name is required for new guardian'});
         return;
      }

      setLoading(true);
      setErrors({});
      try {
         const req = {
            relationship: guardianForm.relationship,
            primaryContact: guardianForm.primaryContact,
            canPickup: guardianForm.canPickup,
            emergencyContact: guardianForm.emergencyContact
         };
         if (guardianForm.mode === 'existing') {
            req.guardianId = guardianForm.guardianId;
         } else {
            req.newGuardian = guardianForm.newGuardian;
         }

         await studentApi.addGuardian(student.id, req);
         const fresh = await studentApi.getById(student.id);
         setLocalGuardians(fresh.guardians || []);
         setIsAddingGuardian(false);
         setGuardianForm({
            mode: 'new',
            guardianId: '',
            newGuardian: {fullName: '', phone: '', email: '', occupation: ''},
            relationship: 'FATHER',
            primaryContact: false,
            canPickup: false,
            emergencyContact: false
         });
      } catch (err) {
         setErrors({guardian: err.message || 'Failed to add guardian'});
      } finally {
         setLoading(false);
      }
   };

   const handleRemoveGuardian = async (guardianId) => {
      if (!window.confirm('Are you sure you want to unlink this guardian?')) return;
      setLoading(true);
      try {
         await studentApi.removeGuardian(student.id, guardianId);
         const fresh = await studentApi.getById(student.id);
         setLocalGuardians(fresh.guardians || []);
      } catch (err) {
         setErrors({general: err.message || 'Failed to remove guardian'});
      } finally {
         setLoading(false);
      }
   };

   return (
       <Modal isOpen={isOpen} onClose={onClose} title={isEdit ? 'Edit Student' : 'Add Student'}>
          <form onSubmit={handleSubmit} className="space-y-6">
             {errors.general && (
                 <div className="bg-red-50 border border-red-200 text-red-700 px-3 py-2 rounded-md text-sm">
                    {errors.general}
                 </div>
             )}

             <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                   <label className="block text-sm font-medium text-slate-700 mb-1">Full Name *</label>
                   <input
                       name="fullName"
                       value={formData.fullName}
                       onChange={handleInputChange}
                       className={`w-full border rounded-md px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-500 outline-none ${errors.fullName ? 'border-red-300' : 'border-slate-300'}`}
                       required={!isEdit}
                   />
                   {errors.fullName && <p className="text-red-600 text-xs mt-1">{errors.fullName}</p>}
                </div>

                {!isEdit && (
                    <div>
                       <label className="block text-sm font-medium text-slate-700 mb-1">Email *</label>
                       <input
                           name="email"
                           type="email"
                           value={formData.email}
                           onChange={handleInputChange}
                           className={`w-full border rounded-md px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-500 outline-none ${errors.email ? 'border-red-300' : 'border-slate-300'}`}
                           required
                       />
                       {errors.email && <p className="text-red-600 text-xs mt-1">{errors.email}</p>}
                    </div>
                )}

                <div>
                   <label className="block text-sm font-medium text-slate-700 mb-1">Phone</label>
                   <input
                       name="phone"
                       value={formData.phone}
                       onChange={handleInputChange}
                       className="w-full border border-slate-300 rounded-md px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-500 outline-none"
                   />
                </div>

                {!isEdit && (
                    <div>
                       <label className="block text-sm font-medium text-slate-700 mb-1">School *</label>
                       <select
                           name="schoolId"
                           value={formData.schoolId}
                           onChange={handleInputChange}
                           className={`w-full border rounded-md px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-500 outline-none ${errors.schoolId ? 'border-red-300' : 'border-slate-300'}`}
                           required
                       >
                          <option value="">Select School</option>
                          {schools.map(s => <option key={s.id} value={s.id}>{s.name}</option>)}
                       </select>
                       {errors.schoolId && <p className="text-red-600 text-xs mt-1">{errors.schoolId}</p>}
                    </div>
                )}

                {!isEdit && (
                    <div>
                       <label className="block text-sm font-medium text-slate-700 mb-1">Admission No *</label>
                       <input
                           name="admissionNo"
                           value={formData.admissionNo}
                           onChange={handleInputChange}
                           className={`w-full border rounded-md px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-500 outline-none ${errors.admissionNo ? 'border-red-300' : 'border-slate-300'}`}
                           required
                       />
                       {errors.admissionNo && <p className="text-red-600 text-xs mt-1">{errors.admissionNo}</p>}
                    </div>
                )}

                <div>
                   <label className="block text-sm font-medium text-slate-700 mb-1">Class Section ID
                      (Optional)</label>
                   <input
                       name="classSectionId"
                       value={formData.classSectionId}
                       onChange={handleInputChange}
                       placeholder="UUID"
                       className="w-full border border-slate-300 rounded-md px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-500 outline-none"
                   />
                </div>

                <div>
                   <label className="block text-sm font-medium text-slate-700 mb-1">Date of Birth</label>
                   <input
                       type="date"
                       name="dateOfBirth"
                       value={formData.dateOfBirth}
                       onChange={handleInputChange}
                       className="w-full border border-slate-300 rounded-md px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-500 outline-none"
                   />
                </div>

                <div>
                   <label className="block text-sm font-medium text-slate-700 mb-1">Gender</label>
                   <select
                       name="gender"
                       value={formData.gender}
                       onChange={handleInputChange}
                       className="w-full border border-slate-300 rounded-md px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-500 outline-none"
                   >
                      <option value="">Select Gender</option>
                      <option value="MALE">Male</option>
                      <option value="FEMALE">Female</option>
                      <option value="OTHER">Other</option>
                      <option value="PREFER_NOT_TO_SAY">Prefer not to say</option>
                   </select>
                </div>

                {!isEdit && (
                    <div>
                       <label className="block text-sm font-medium text-slate-700 mb-1">Admission Date</label>
                       <input
                           type="date"
                           name="admissionDate"
                           value={formData.admissionDate}
                           onChange={handleInputChange}
                           className="w-full border border-slate-300 rounded-md px-3 py-2 text-sm focus:ring-2 focus:ring-indigo-500 outline-none"
                       />
                    </div>
                )}

                {isEdit && (
                    <div className="flex items-center mt-6">
                       <input
                           type="checkbox"
                           name="active"
                           id="active"
                           checked={formData.active}
                           onChange={handleInputChange}
                           className="h-4 w-4 text-indigo-600 border-slate-300 rounded focus:ring-indigo-500"
                       />
                       <label htmlFor="active" className="ml-2 block text-sm text-slate-700">Active</label>
                    </div>
                )}
             </div>

             {/* Guardians Section */}
             <div className="border-t border-slate-200 pt-4">
                <div className="flex items-center justify-between mb-3">
                   <h3 className="text-sm font-semibold text-slate-900">Guardians</h3>
                   {!isEdit && (
                       <button
                           type="button"
                           onClick={handleAddGuardianSlot}
                           className="text-xs text-indigo-600 hover:text-indigo-800 font-medium"
                       >
                          + Add Guardian
                       </button>
                   )}
                </div>

                {isEdit ? (
                    <div className="space-y-3">
                       {localGuardians.length === 0 && (
                           <p className="text-sm text-slate-500 italic">No guardians linked.</p>
                       )}
                       {localGuardians.map((g) => (
                           <div key={g.guardianId}
                                className="flex items-start justify-between bg-slate-50 p-3 rounded-md border border-slate-200">
                              <div>
                                 <div className="font-medium text-sm text-slate-900">{g.fullName} <span
                                     className="text-xs text-slate-500">({g.relationship})</span></div>
                                 <div className="text-xs text-slate-600">{g.phone} {g.email && `• ${g.email}`}</div>
                                 <div className="text-xs text-slate-500 mt-1">
                                    {g.primaryContact && 'Primary • '}
                                    {g.canPickup && 'Can Pickup • '}
                                    {g.emergencyContact && 'Emergency'}
                                 </div>
                              </div>
                              <button
                                  type="button"
                                  onClick={() => handleRemoveGuardian(g.guardianId)}
                                  disabled={loading}
                                  className="text-red-600 hover:text-red-800 text-xs font-medium disabled:opacity-50"
                              >
                                 Unlink
                              </button>
                           </div>
                       ))}

                       {!isAddingGuardian ? (
                           <button
                               type="button"
                               onClick={() => setIsAddingGuardian(true)}
                               className="w-full py-2 border-2 border-dashed border-slate-300 rounded-md text-sm text-slate-600 hover:border-indigo-400 hover:text-indigo-600 transition-colors"
                           >
                              + Link or Add Guardian
                           </button>
                       ) : (
                           <div className="bg-slate-50 p-4 rounded-md border border-slate-200 space-y-3">
                              <div className="flex gap-4 mb-2">
                                 <label className="flex items-center gap-2 text-sm">
                                    <input
                                        type="radio"
                                        name="gMode"
                                        checked={guardianForm.mode === 'new'}
                                        onChange={() => handleGuardianFormChange('mode', 'new')}
                                        className="text-indigo-600"
                                    />
                                    Create New
                                 </label>
                                 <label className="flex items-center gap-2 text-sm">
                                    <input
                                        type="radio"
                                        name="gMode"
                                        checked={guardianForm.mode === 'existing'}
                                        onChange={() => handleGuardianFormChange('mode', 'existing')}
                                        className="text-indigo-600"
                                    />
                                    Link Existing (by ID)
                                 </label>
                              </div>

                              {guardianForm.mode === 'existing' ? (
                                  <div>
                                     <label className="block text-xs font-medium text-slate-700 mb-1">Guardian UUID
                                        *</label>
                                     <input
                                         value={guardianForm.guardianId}
                                         onChange={(e) => handleGuardianFormChange('guardianId', e.target.value)}
                                         className="w-full border border-slate-300 rounded-md px-2 py-1.5 text-sm outline-none focus:ring-1 focus:ring-indigo-500"
                                         placeholder="Enter existing guardian UUID"
                                     />
                                  </div>
                              ) : (
                                  <div className="grid grid-cols-2 gap-3">
                                     <input
                                         placeholder="Full Name *"
                                         value={guardianForm.newGuardian.fullName}
                                         onChange={(e) => handleGuardianFormChange('newGuardian.fullName', e.target.value)}
                                         className="border border-slate-300 rounded-md px-2 py-1.5 text-sm outline-none focus:ring-1 focus:ring-indigo-500"
                                     />
                                     <input
                                         placeholder="Phone"
                                         value={guardianForm.newGuardian.phone}
                                         onChange={(e) => handleGuardianFormChange('newGuardian.phone', e.target.value)}
                                         className="border border-slate-300 rounded-md px-2 py-1.5 text-sm outline-none focus:ring-1 focus:ring-indigo-500"
                                     />
                                     <input
                                         placeholder="Email"
                                         value={guardianForm.newGuardian.email}
                                         onChange={(e) => handleGuardianFormChange('newGuardian.email', e.target.value)}
                                         className="border border-slate-300 rounded-md px-2 py-1.5 text-sm outline-none focus:ring-1 focus:ring-indigo-500"
                                     />
                                     <input
                                         placeholder="Occupation"
                                         value={guardianForm.newGuardian.occupation}
                                         onChange={(e) => handleGuardianFormChange('newGuardian.occupation', e.target.value)}
                                         className="border border-slate-300 rounded-md px-2 py-1.5 text-sm outline-none focus:ring-1 focus:ring-indigo-500"
                                     />
                                  </div>
                              )}

                              <div>
                                 <label
                                     className="block text-xs font-medium text-slate-700 mb-1">Relationship</label>
                                 <select
                                     value={guardianForm.relationship}
                                     onChange={(e) => handleGuardianFormChange('relationship', e.target.value)}
                                     className="w-full border border-slate-300 rounded-md px-2 py-1.5 text-sm outline-none focus:ring-1 focus:ring-indigo-500"
                                 >
                                    <option value="FATHER">Father</option>
                                    <option value="MOTHER">Mother</option>
                                    <option value="GRANDPARENT">Grandparent</option>
                                    <option value="SIBLING">Sibling</option>
                                    <option value="LEGAL_GUARDIAN">Legal Guardian</option>
                                    <option value="OTHER">Other</option>
                                 </select>
                              </div>

                              <div className="flex flex-wrap gap-4">
                                 <label className="flex items-center gap-2 text-xs text-slate-700">
                                    <input
                                        type="checkbox"
                                        checked={guardianForm.primaryContact}
                                        onChange={(e) => handleGuardianFormChange('primaryContact', e.target.checked)}
                                        className="rounded border-slate-300 text-indigo-600 focus:ring-indigo-500"
                                    />
                                    Primary Contact
                                 </label>
                                 <label className="flex items-center gap-2 text-xs text-slate-700">
                                    <input
                                        type="checkbox"
                                        checked={guardianForm.canPickup}
                                        onChange={(e) => handleGuardianFormChange('canPickup', e.target.checked)}
                                        className="rounded border-slate-300 text-indigo-600 focus:ring-indigo-500"
                                    />
                                    Can Pickup
                                 </label>
                                 <label className="flex items-center gap-2 text-xs text-slate-700">
                                    <input
                                        type="checkbox"
                                        checked={guardianForm.emergencyContact}
                                        onChange={(e) => handleGuardianFormChange('emergencyContact', e.target.checked)}
                                        className="rounded border-slate-300 text-indigo-600 focus:ring-indigo-500"
                                    />
                                    Emergency Contact
                                 </label>
                              </div>

                              {errors.guardian && (
                                  <p className="text-red-600 text-xs">{errors.guardian}</p>
                              )}

                              <div className="flex gap-2 pt-2">
                                 <button
                                     type="button"
                                     onClick={handleAddGuardianToExisting}
                                     disabled={loading}
                                     className="bg-indigo-600 text-white px-3 py-1.5 rounded-md text-xs font-medium hover:bg-indigo-700 disabled:opacity-50"
                                 >
                                    Save Guardian
                                 </button>
                                 <button
                                     type="button"
                                     onClick={() => {
                                        setIsAddingGuardian(false);
                                        setErrors(prev => ({...prev, guardian: ''}));
                                     }}
                                     className="text-slate-600 px-3 py-1.5 rounded-md text-xs font-medium hover:bg-slate-100"
                                 >
                                    Cancel
                                 </button>
                              </div>
                           </div>
                       )}
                    </div>
                ) : (
                    <div className="space-y-3">
                       {guardianSlots.map((g) => (
                           <div key={g.id} className="bg-slate-50 p-3 rounded-md border border-slate-200 space-y-3">
                              <div className="flex justify-between items-center">
                                 <span className="text-xs font-semibold text-slate-700">Guardian</span>
                                 <button
                                     type="button"
                                     onClick={() => handleRemoveGuardianSlot(g.id)}
                                     className="text-red-600 hover:text-red-800 text-xs"
                                 >
                                    Remove
                                 </button>
                              </div>

                              <div className="flex gap-4">
                                 <label className="flex items-center gap-2 text-sm">
                                    <input
                                        type="radio"
                                        checked={g.mode === 'new'}
                                        onChange={() => handleGuardianSlotChange(g.id, 'mode', 'new')}
                                        className="text-indigo-600"
                                    />
                                    New
                                 </label>
                                 <label className="flex items-center gap-2 text-sm">
                                    <input
                                        type="radio"
                                        checked={g.mode === 'existing'}
                                        onChange={() => handleGuardianSlotChange(g.id, 'mode', 'existing')}
                                        className="text-indigo-600"
                                    />
                                    Existing
                                 </label>
                              </div>

                              {g.mode === 'existing' ? (
                                  <input
                                      placeholder="Guardian UUID *"
                                      value={g.guardianId}
                                      onChange={(e) => handleGuardianSlotChange(g.id, 'guardianId', e.target.value)}
                                      className="w-full border border-slate-300 rounded-md px-2 py-1.5 text-sm outline-none focus:ring-1 focus:ring-indigo-500"
                                  />
                              ) : (
                                  <div className="grid grid-cols-2 gap-3">
                                     <input
                                         placeholder="Full Name *"
                                         value={g.newGuardian.fullName}
                                         onChange={(e) => handleGuardianSlotChange(g.id, 'newGuardian.fullName', e.target.value)}
                                         className="border border-slate-300 rounded-md px-2 py-1.5 text-sm outline-none focus:ring-1 focus:ring-indigo-500"
                                     />
                                     <input
                                         placeholder="Phone"
                                         value={g.newGuardian.phone}
                                         onChange={(e) => handleGuardianSlotChange(g.id, 'newGuardian.phone', e.target.value)}
                                         className="border border-slate-300 rounded-md px-2 py-1.5 text-sm outline-none focus:ring-1 focus:ring-indigo-500"
                                     />
                                     <input
                                         placeholder="Email"
                                         value={g.newGuardian.email}
                                         onChange={(e) => handleGuardianSlotChange(g.id, 'newGuardian.email', e.target.value)}
                                         className="border border-slate-300 rounded-md px-2 py-1.5 text-sm outline-none focus:ring-1 focus:ring-indigo-500"
                                     />
                                     <input
                                         placeholder="Occupation"
                                         value={g.newGuardian.occupation}
                                         onChange={(e) => handleGuardianSlotChange(g.id, 'newGuardian.occupation', e.target.value)}
                                         className="border border-slate-300 rounded-md px-2 py-1.5 text-sm outline-none focus:ring-1 focus:ring-indigo-500"
                                     />
                                  </div>
                              )}

                              <select
                                  value={g.relationship}
                                  onChange={(e) => handleGuardianSlotChange(g.id, 'relationship', e.target.value)}
                                  className="w-full border border-slate-300 rounded-md px-2 py-1.5 text-sm outline-none focus:ring-1 focus:ring-indigo-500"
                              >
                                 <option value="FATHER">Father</option>
                                 <option value="MOTHER">Mother</option>
                                 <option value="GRANDPARENT">Grandparent</option>
                                 <option value="SIBLING">Sibling</option>
                                 <option value="LEGAL_GUARDIAN">Legal Guardian</option>
                                 <option value="OTHER">Other</option>
                              </select>

                              <div className="flex flex-wrap gap-4">
                                 <label className="flex items-center gap-2 text-xs text-slate-700">
                                    <input
                                        type="checkbox"
                                        checked={g.primaryContact}
                                        onChange={(e) => handleGuardianSlotChange(g.id, 'primaryContact', e.target.checked)}
                                        className="rounded border-slate-300 text-indigo-600 focus:ring-indigo-500"
                                    />
                                    Primary
                                 </label>
                                 <label className="flex items-center gap-2 text-xs text-slate-700">
                                    <input
                                        type="checkbox"
                                        checked={g.canPickup}
                                        onChange={(e) => handleGuardianSlotChange(g.id, 'canPickup', e.target.checked)}
                                        className="rounded border-slate-300 text-indigo-600 focus:ring-indigo-500"
                                    />
                                    Pickup
                                 </label>
                                 <label className="flex items-center gap-2 text-xs text-slate-700">
                                    <input
                                        type="checkbox"
                                        checked={g.emergencyContact}
                                        onChange={(e) => handleGuardianSlotChange(g.id, 'emergencyContact', e.target.checked)}
                                        className="rounded border-slate-300 text-indigo-600 focus:ring-indigo-500"
                                    />
                                    Emergency
                                 </label>
                              </div>
                           </div>
                       ))}
                       {guardianSlots.length === 0 && (
                           <p className="text-sm text-slate-500 italic">No guardians added yet.</p>
                       )}
                    </div>
                )}
             </div>

             <div className="flex justify-end gap-3 pt-4 border-t border-slate-200">
                <button
                    type="button"
                    onClick={onClose}
                    className="px-4 py-2 border border-slate-300 rounded-md text-sm font-medium text-slate-700 hover:bg-slate-50"
                >
                   Cancel
                </button>
                <button
                    type="submit"
                    disabled={loading}
                    className="px-4 py-2 bg-indigo-600 text-white rounded-md text-sm font-medium hover:bg-indigo-700 disabled:opacity-50"
                >
                   {loading ? 'Saving...' : (isEdit ? 'Save Changes' : 'Create Student')}
                </button>
             </div>
          </form>
       </Modal>
   );
}