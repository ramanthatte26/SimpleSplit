import React, { useEffect, useState } from 'react';
import { getBalances, getTransactions } from '../services/api';
import { Link } from 'react-router-dom';

const Dashboard = () => {
  const [balances, setBalances] = useState({});
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    try {
      const [balanceRes, transactionRes] = await Promise.all([
        getBalances(),
        getTransactions()
      ]);
      setBalances(balanceRes.data);
      setTransactions(transactionRes.data);
      setLoading(false);
    } catch (error) {
      console.error("Error fetching dashboard data", error);
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="grid-2">
        <div className="card">
          <div className="flex justify-between items-center mb-4">
            <h2>Balances</h2>
            <Link to="/add-transaction" className="btn btn-primary" style={{ fontSize: '0.8rem' }}>+ Add Expense</Link>
          </div>
          {loading ? <p>Loading...</p> : (
            <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
              {Object.entries(balances).length === 0 ? (
                 <p style={{ color: '#6b7280' }}>No balances yet.</p>
              ) : (
                Object.entries(balances).map(([username, amount]) => (
                  <div key={username} style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    padding: '12px',
                    borderRadius: '8px',
                    backgroundColor: '#f9fafb',
                    borderLeft: `4px solid ${amount >= 0 ? 'var(--secondary-color)' : 'var(--danger-color)'}`
                  }}>
                    <span style={{ fontWeight: '600' }}>{username}</span>
                    <span className={amount >= 0 ? 'text-success' : 'text-danger'}>
                      {amount >= 0 ? `+${amount.toFixed(2)}` : amount.toFixed(2)}
                    </span>
                  </div>
                ))
              )}
            </div>
          )}
        </div>

        <div className="card">
          <h2>Recent Transactions</h2>
          {loading ? <p>Loading...</p> : (
            <div className="table-container" style={{ maxHeight: '400px', overflowY: 'auto' }}>
              <table>
                <thead>
                  <tr>
                    <th>Date</th>
                    <th>Description</th>
                    <th>Payer</th>
                    <th>Amount</th>
                  </tr>
                </thead>
                <tbody>
                  {transactions.slice().reverse().map(t => (
                    <tr key={t.id}>
                      <td>{t.date}</td>
                      <td>{t.description}</td>
                      <td>{t.payer.username}</td>
                      <td style={{ fontWeight: 'bold' }}>${t.amount.toFixed(2)}</td>
                    </tr>
                  ))}
                  {transactions.length === 0 && (
                     <tr><td colSpan="4" style={{textAlign: 'center', color: '#6b7280'}}>No transactions found.</td></tr>
                  )}
                </tbody>
              </table>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
