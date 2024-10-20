// src/Stripe.js
import { loadStripe } from "@stripe/stripe-js";

const stripePromise = loadStripe(
  "pk_test_51Pdgl8RuVIC7U6kcnDPmtuBFtSoX83Of4rWP09F9M6LkmxUPVCRgi0eRY5aAMXsxBOhCtlVpnF6JfUSRpF7NdHAh00DKHrJPs6"
); // Replace with your actual publishable key

export default stripePromise;
