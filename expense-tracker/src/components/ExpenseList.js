import React, { useState } from 'react';

function ExpenseList({ expenses }) {
  const [sortType, setSortType] = useState('date');

  const sortedExpenses = [...expenses].sort((a, b) => {
    if (sortType === 'amount') {
      return b.amount - a.amount;  // Sort by amount (descending)
    } else if (sortType === 'category') {
      return a.category.localeCompare(b.category);  // Sort by category (alphabetically)
    } else {
      return new Date(b.date) - new Date(a.date);  // Sort by date (most recent first)
    }
  });

  return (
    <div className="expense-list-container">
      <h2>Expense List</h2>
      
      {/* Sorting Dropdown */}
      <div>
        <label htmlFor="sortSelect">Sort by: </label>
        <select
          id="sortSelect"
          onChange={(e) => setSortType(e.target.value)}
          value={sortType}
        >
          <option value="date">Date</option>
          <option value="amount">Amount</option>
          <option value="category">Category</option>
        </select>
      </div>

      {/* Expense List Table */}
      <table className="expense-list">
        <thead>
          <tr>
            <th>Category</th>
            <th>Amount</th>
            <th>Date</th>
          </tr>
        </thead>
        <tbody>
          {sortedExpenses.map((expense) => (
            <tr key={expense.id}>
              <td>{expense.category}</td>
              <td>${expense.amount}</td>
              <td>{expense.date}</td>
            </tr>
          ))}
        </tbody>
      </table>

      {/* Total Expense */}
      <p className="total-expense">
        Total Expense: ${expenses.reduce((acc, curr) => acc + curr.amount, 0)}
      </p>
    </div>
  );
}

export default ExpenseList;
