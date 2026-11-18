const form = document.getElementById('predictionForm');
const errorMessage = document.getElementById('errorMessage');
const submitButton = document.getElementById('submitButton');

form.addEventListener('submit', async (event) => {
  event.preventDefault(); errorMessage.textContent = '';
  submitButton.disabled = true; submitButton.textContent = 'Processing...';
  const payload = {
    cropType: document.getElementById('cropType').value,
    soilMoisture: Number(document.getElementById('soilMoisture').value),
    temperature: Number(document.getElementById('temperature').value),
    humidity: Number(document.getElementById('humidity').value),
    rainfall: Number(document.getElementById('rainfall').value)
  };
  try {
    const response = await fetch('/api/predictions', {method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify(payload)});
    const data = await response.json();
    if (!response.ok) throw new Error(data.error || 'Unable to generate recommendation.');
    showResult(data); await loadHistory();
  } catch (error) { errorMessage.textContent = error.message || 'Could not connect to the server.'; }
  finally { submitButton.disabled = false; submitButton.innerHTML = 'Generate recommendation <span>→</span>'; }
});

function showResult(data) {
  document.getElementById('resultIcon').textContent = data.irrigationNeeded ? '!' : '✓';
  document.getElementById('resultTitle').textContent = data.status;
  document.getElementById('resultText').textContent = data.recommendation;
  document.getElementById('resultDetails').textContent = `${data.cropType} · Soil moisture ${data.soilMoisture}% · Temperature ${data.temperature}°C · Humidity ${data.humidity}% · Rainfall ${data.rainfall} mm`;
}

async function loadHistory() {
  const body = document.getElementById('historyBody');
  try {
    const response = await fetch('/api/predictions'); if (!response.ok) throw new Error('History unavailable');
    const records = await response.json();
    if (!records.length) { body.innerHTML = '<tr><td colspan="6" class="empty">No records yet. Submit the form to add one.</td></tr>'; return; }
    body.replaceChildren();
    records.forEach(item => {
      const tr = document.createElement('tr');
      [item.cropType,`${item.soilMoisture}%`,`${item.temperature}°C`,`${item.rainfall} mm`,item.status,item.createdAt].forEach((value,index) => {
        const td = document.createElement('td'); td.textContent = value;
        if (index === 4) td.className = item.irrigationNeeded ? 'status-needed' : 'status-ok';
        tr.appendChild(td);
      }); body.appendChild(tr);
    });
  } catch { body.innerHTML = '<tr><td colspan="6" class="empty">Could not load history. Check the backend server.</td></tr>'; }
}
loadHistory();
