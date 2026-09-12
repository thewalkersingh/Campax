import {useEffect, useState} from "react";
import Modal from "./Modal";
import {createSchool, updateSchool} from "../lib/schoolApi";
import {ApiError} from "../lib/apiClient";

const emptyForm = {name: "", subdomain: "", planTier: "standard"};

export default function SchoolFormModal({open, school, onClose, onSaved}) {
   const isEdit = Boolean(school);
   const [form, setForm] = useState(emptyForm);
   const [fieldErrors, setFieldErrors] = useState({});
   const [generalError, setGeneralError] = useState("");
   const [submitting, setSubmitting] = useState(false);

   useEffect(() => {
      if (open) {
         setForm(
             school
                 ? {name: school.name, subdomain: school.subdomain, planTier: school.planTier}
                 : emptyForm
         );
         setFieldErrors({});
         setGeneralError("");
      }
   }, [open, school]);

   function handleChange(field, value) {
      setForm((prev) => ({...prev, [field]: value}));
   }

   async function handleSubmit(e) {
      e.preventDefault();
      setSubmitting(true);
      setFieldErrors({});
      setGeneralError("");
      try {
         if (isEdit) {
            // subdomain is immutable after creation - matches the backend's design
            await updateSchool(school.id, {name: form.name, planTier: form.planTier});
         } else {
            await createSchool(form);
         }
         onSaved();
      } catch (err) {
         if (err instanceof ApiError && err.details?.fieldErrors) {
            setFieldErrors(err.details.fieldErrors);
         } else if (err instanceof ApiError) {
            setGeneralError(err.message);
         } else {
            setGeneralError("Something went wrong. Please try again.");
         }
      } finally {
         setSubmitting(false);
      }
   }

   return (
       <Modal
           open={open}
           title={isEdit ? "Edit School" : "Add School"}
           onClose={onClose}
           footer={
              <>
                 <button
                     type="button"
                     onClick={onClose}
                     className="px-3 py-1.5 text-sm font-medium rounded-md border border-slate-300 text-slate-700 hover:bg-slate-50"
                 >
                    Cancel
                 </button>
                 <button
                     type="submit"
                     form="school-form"
                     disabled={submitting}
                     className="px-3 py-1.5 text-sm font-medium rounded-md bg-indigo-600 text-white hover:bg-indigo-700 disabled:opacity-50"
                 >
                    {submitting ? "Saving..." : isEdit ? "Save changes" : "Create school"}
                 </button>
              </>
           }
       >
          <form id="school-form" onSubmit={handleSubmit} className="space-y-4">
             {generalError && (
                 <div className="rounded-md bg-red-50 border border-red-200 text-red-700 text-sm px-3 py-2">
                    {generalError}
                 </div>
             )}

             <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">School name</label>
                <input
                    type="text"
                    value={form.name}
                    onChange={(e) => handleChange("name", e.target.value)}
                    className="w-full rounded-md border border-slate-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                    required
                />
                {fieldErrors.name && <p className="text-xs text-red-600 mt-1">{fieldErrors.name}</p>}
             </div>

             <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">
                   Subdomain{" "}
                   {isEdit && <span className="text-slate-400 font-normal">(cannot be changed)</span>}
                </label>
                <input
                    type="text"
                    value={form.subdomain}
                    onChange={(e) => handleChange("subdomain", e.target.value)}
                    disabled={isEdit}
                    placeholder="e.g. green-valley"
                    className="w-full rounded-md border border-slate-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500 disabled:bg-slate-50 disabled:text-slate-400"
                    required
                />
                {fieldErrors.subdomain && (
                    <p className="text-xs text-red-600 mt-1">{fieldErrors.subdomain}</p>
                )}
             </div>

             <div>
                <label className="block text-sm font-medium text-slate-700 mb-1">Plan tier</label>
                <select
                    value={form.planTier}
                    onChange={(e) => handleChange("planTier", e.target.value)}
                    className="w-full rounded-md border border-slate-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-indigo-500"
                >
                   <option value="FREE">Free</option>
                   <option value="STANDARD">Standard</option>
                   <option value="PREMIUM">Premium</option>
                </select>
                {fieldErrors.planTier && (
                    <p className="text-xs text-red-600 mt-1">{fieldErrors.planTier}</p>
                )}
             </div>
          </form>
       </Modal>
   );
}