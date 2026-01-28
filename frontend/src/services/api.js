import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

export const getUsers = () => axios.get(`${API_URL}/users`);
export const createUser = (user) => axios.post(`${API_URL}/users`, user);

export const getTransactions = () => axios.get(`${API_URL}/transactions`);
export const createTransaction = (transaction) => axios.post(`${API_URL}/transactions`, transaction);
export const getBalances = () => axios.get(`${API_URL}/transactions/balances`);
