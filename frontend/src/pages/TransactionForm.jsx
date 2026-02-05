import React, { useEffect, useState } from 'react';
import { getUsers, createTransaction } from '../services/api';
import { useNavigate } from 'react-router-dom';

const TransactionForm = () => {
  const navigate = useNavigate();
  const [users, setUsers] = useState([]);
  const [formData, setFormData] = useState({
    description: '',
    amount: '',
    payerId: '',
    date: new Date().toISOString().split('T')[0]
  });
  const [selectedParticipants, setSelectedParticipants] = useState([]);

  useEffect(() => {
    getUsers().then(res => setUsers(res.data));
  }, []);

  const handleParticipantToggle = (userId) => {
    if (selectedParticipants.includes(userId)) {
      setSelectedParticipants(selectedParticipants.filter(id => id !== userId));
    } else {
      setSelectedParticipants([...selectedParticipants, userId]);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.payerId || selectedParticipants.length === 0) {
      alert("Please select a payer and at least one participant.");
      return;
    }

    const totalAmount = parseFloat(formData.amount);
    const splitAmount = totalAmount / (selectedParticipants.length);
    // Note: Usually the payer is also a participant, but for simplicity let's say the split is among selected participants.
    // Ideally, we should include the payer in the split if they also consumed the service.
    // Let's assume the selected participants owe the payer.

    const splitRules = selectedParticipants.map(userId => ({
      user: { id: userId },
      amount: splitAmount
    }));

    const payload = {
      description: formData.description,
      amount: totalAmount,
      date: formData.date,
      payer: { id: formData.payerId },
      splitRules: splitRules
    };

    try {
      await createTransaction(payload);
      navigate('/');
    } catch (error) {
      console.error("Error creating transaction", error);
      alert("Failed to create transaction.");
    }
  };

  return (
    <div className="card" style={{ maxWidth: '600px', margin: '0 auto' }}>
      <h2>Add New Expense</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label className="form-label">Description</label>
          <input
            type="text"
            className="form-input"
            value={formData.description}
            onChange={e => setFormData({...formData, description: e.target.value})}
            required
            placeholder="e.g. Dinner at Mario's"
          />
        </div>

        <div className="grid-2">
          <div className="form-group">
            <label className="form-label">Amount ($)</label>
            <input
              type="number"
              step="0.01"
              className="form-input"
              value={formData.amount}
              onChange={e => setFormData({...formData, amount: e.target.value})}
              required
              placeholder="0.00"
            />
          </div>
          <div className="form-group">
            <label className="form-label">Date</label>
            <input
              type="date"
              className="form-input"
              value={formData.date}
              onChange={e => setFormData({...formData, date: e.target.value})}
              required
            />
          </div>
        </div>

        <div className="form-group">
          <label className="form-label">Who Paid?</label>
          <select
            className="form-select"
            value={formData.payerId}
            onChange={e => setFormData({...formData, payerId: e.target.value})}
            required
          >
            <option value="">Select Payer</option>
            {users.map(u => (
              <option key={u.id} value={u.id}>{u.username}</option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label className="form-label">Split With (Select participants who owe money)</label>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '10px', marginTop: '10px' }}>
            {users.map(u => (
              <label key={u.id} style={{
                display: 'flex',
                alignItems: 'center',
                padding: '10px',
                border: selectedParticipants.includes(u.id) ? '1px solid var(--primary-color)' : '1px solid #e5e7eb',
                borderRadius: '8px',
                cursor: 'pointer',
                backgroundColor: selectedParticipants.includes(u.id) ? '#eef2ff' : 'white'
              }}>
                <input
                  type="checkbox"
                  checked={selectedParticipants.includes(u.id)}
                  onChange={() => handleParticipantToggle(u.id)}
                  style={{ marginRight: '10px' }}
                />
                {u.username}
              </label>
            ))}
          </div>
          <p style={{ fontSize: '0.9rem', color: 'var(--text-secondary)', marginTop: '8px' }}>
            {formData.amount && selectedParticipants.length > 0 && (
              `Each person owes: $${(parseFloat(formData.amount) / selectedParticipants.length).toFixed(2)}`
            )}
          </p>
        </div>

        <button type="submit" className="btn btn-primary" style={{ width: '100%', padding: '12px' }}>
          Create Transaction
        </button>
      </form>
    </div>
  );
};

export default TransactionForm;
