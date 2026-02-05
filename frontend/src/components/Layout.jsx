import React from 'react';
import { Link, useLocation } from 'react-router-dom';

const Layout = ({ children }) => {
  const location = useLocation();

  const isActive = (path) => {
    return location.pathname === path ? 'nav-link active' : 'nav-link';
  };

  return (
    <div>
      <nav className="navbar">
        <div className="container nav-content">
          <Link to="/" className="brand">
            <span style={{ fontSize: '1.8rem' }}>💸</span> SimpleSplit
          </Link>
          <div className="nav-links">
            <Link to="/" className={isActive('/')}>Dashboard</Link>
            <Link to="/users" className={isActive('/users')}>Users</Link>
            <Link to="/add-transaction" className={isActive('/add-transaction')}>Add Expense</Link>
          </div>
        </div>
      </nav>
      <div className="container">
        {children}
      </div>
    </div>
  );
};

export default Layout;
