const initialState = {
  messages: [],
};

const messagesReducer = (state = initialState, action) => {
  var messages = "";
  switch (action.type) {
    case "ADD_MESSAGE":
      messages = state.messages;
      var founded = false;
      messages.forEach((localMessage) => {
        if (
          localMessage.message === action.message.message &&
          localMessage.variant === action.message.variant
        ) {
          founded = true;
        }
      });
      if (!founded) {
        messages.push(action.message);
      }
      return {
        ...state,
        messages,
      };
    case "REMOVER_MESAJE":
      var currentMessages = state.messages;
      messages = currentMessages.filter((value, index, arr) => {
        return value !== action.message;
      });
      return {
        ...state,
        messages,
      };
    default:
      return state;
  }
};

export default messagesReducer;
