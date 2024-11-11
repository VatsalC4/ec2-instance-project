import React, { useState, useEffect } from 'react';
import ExpenseForm from './components/ExpenseForm';
import ExpenseList from './components/ExpenseList';
import BudgetDisplay from './components/BudgetDisplay';
import './App.css';

function App() {
  const [expenses, setExpenses] = useState([]);
  const [budget, setBudget] = useState(0);

  const addExpense = (expense) => {
    setExpenses([...expenses, expense]);
  };

  const totalExpenses = expenses.reduce((acc, curr) => acc + curr.amount, 0);
  const remainingBudget = budget - totalExpenses;

  useEffect(() => {
    if (remainingBudget <= budget * 0.2 && remainingBudget > 0) {
      // Show browser alert only when budget is below 20% and greater than 0
      alert("Warning: 80% of your budget has been utilized!");
    }
  }, [remainingBudget, budget]);

  return (
    <div className="App">
      <h1>Expense Tracker</h1>
      <BudgetDisplay 
        budget={budget} 
        setBudget={setBudget} 
        remainingBudget={remainingBudget} 
      />
      <ExpenseForm addExpense={addExpense} />
      <ExpenseList expenses={expenses} />
    </div>
  );
}

export default App;
