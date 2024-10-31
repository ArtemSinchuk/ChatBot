Features:

- updated Config.java
- added retry mechanism when a request fails
- added NetworkUtil.java and checking the network connection
- added checks for data and fields
- added full chat history handling: the entire conversation context (user and assistant messages) is now sent to the ChatGPT API in each request.
- The chatbot responds based on the full context of the dialog
- ChatGPTClient: Enhanced error handling by splitting the processing into multiple types of exceptions
- Added: ChatMessage.java, ChatRequest.java, ChatResponse.java, ChatServlet.java and SimpleHttpServer.java
- Integrated backend Java server (SimpleHttpServer.java) with the frontend interface
- Added asynchronous request handling
- The server now correctly handles and logs POST requests from the frontend.
- Enhanced frontend (app.js) to work seamlessly with the Java backend server for sending and receiving user queries.
- Streamlined Java server routes to handle `/api/chat` request for chatbot communication.
- Improved feedback to the user when the server is not available or the network connection is down
