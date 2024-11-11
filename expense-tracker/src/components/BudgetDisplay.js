import React, { useState } from 'react';

function BudgetDisplay({ budget, setBudget, remainingBudget }) {
  const [inputBudget, setInputBudget] = useState(budget);

  const handleSetBudget = () => {
    setBudget(Number(inputBudget));
  };

  return (
    <div className="budget-display">
      <h2>Set Monthly Budget</h2>
      <input
        type="number"
        value={inputBudget}
        onChange={(e) => setInputBudget(e.target.value)}
        placeholder="Enter monthly budget"
        className="budget-input"
      />
      <button onClick={handleSetBudget} className="budget-button">Set Budget</button>
      <p>Total Budget: ${budget}</p>
      <p>Remaining Budget: ${remainingBudget}</p>
    </div>
  );
}

export default BudgetDisplay;
