document.getElementById("chatForm").addEventListener("submit", async function (event) {
    event.preventDefault();  // Prevent the default form submission behavior

    const userInput = document.getElementById("userInput").value;
    const responseDiv = document.getElementById("response");

    if (userInput.trim() === "") {
        alert("Please enter a prompt.");  // Check if the input is empty
        return;
    }

    try {
        // Send POST request to Java backend
        fetch("http://localhost:8080/api/chat", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ message: "Hello from frontend" })
        })
        .then(response => response.json())
        .then(data => console.log(data))
        .catch(error => console.error("Error:", error));

        console.log('Response status:', response.status);

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        // Parse the JSON response from the backend
        const data = await response.json();
        responseDiv.textContent = data.reply || "No response from ChatGPT.";  // Display the reply

    } catch (error) {
        console.error('Error occurred:', error);
        responseDiv.textContent = 'Error occurred while communicating with the server.';
    }
});

// Logic for the clear button
document.getElementById("clearButton").addEventListener("click", function () {
    document.getElementById("userInput").value = "";
    this.style.visibility = "hidden";
});

document.getElementById("userInput").addEventListener("input", function () {
    const clearButton = document.getElementById("clearButton");
    clearButton.style.visibility = this.value.length > 0 ? "visible" : "hidden";
});
