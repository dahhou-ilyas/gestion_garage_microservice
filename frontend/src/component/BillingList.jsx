
import { useState, useEffect } from 'react';
import axios from 'axios';
import { BACKEND_URL } from '../../config';

const BillingList = () => {
    const [invoices,setInvoices]=useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    useEffect(()=>{
        const fetchInvoice = async ()=>{
            try{
                const response = await axios.get(`${BACKEND_URL}/billing-service/api/invoices`);
                setInvoices(response.data);
                setLoading(false);
            }catch(err){
                setError('Failed to fetch customers');
                setLoading(false);
            }
        }
        fetchInvoice();
    },[]);

    if (loading) return <div>Loading Invoices...</div>;
    if (error) return <div>{error}</div>;

    return (
        <div className="p-4">
          <h2 className="text-2xl mb-4">Registered Customers</h2>
          <table className="w-full border-collapse border">
            <thead>
              <tr className="bg-gray-200">
                <th className="border p-2">Invoice Number</th>
                <th className="border p-2">customerId</th>
                <th className="border p-2">carId</th>
                <th className="border p-2">issue Date</th>
                <th className="border p-2">Due Date</th>
                <th className="border p-2">Status</th>
                <th className="border p-2">Total</th>
              </tr>
            </thead>
            <tbody>
              {invoices.map((invoice,index) => (
                <tr key={index} className="hover:bg-gray-100">
                  <td className="border p-2">{invoice.invoiceNumber}</td>
                  <td className="border p-2">{invoice.customerId}</td>
                  <td className="border p-2">{invoice.carId}</td>
                  <td className="border p-2">{invoice.issueDate}</td>
                  <td className="border p-2">{invoice.dueDate}</td>
                  <td className="border p-2">{invoice.status}</td>
                  <td className="border p-2">{invoice.total}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      );

}
export default BillingList;