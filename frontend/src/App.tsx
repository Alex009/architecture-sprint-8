import React from 'react';
import { ReactKeycloakProvider } from '@react-keycloak/web';
import Keycloak, {KeycloakConfig, KeycloakInitOptions} from 'keycloak-js';
import ReportPage from './components/ReportPage';

const keycloakConfig: KeycloakConfig = {
  url: process.env.REACT_APP_KEYCLOAK_URL,
  realm: process.env.REACT_APP_KEYCLOAK_REALM||"",
  clientId: process.env.REACT_APP_KEYCLOAK_CLIENT_ID||""
};

const keycloakInitOptions: KeycloakInitOptions = {
  // с включенным iframe инициализация не выполняется вообще
  checkLoginIframe: false,
  /*
  Если это закомментировать то получим ошибки, так как keycloak настроен на обязательное использование PKCE.

[Log] Keycloak event: – "onAuthError" – {error: "invalid_request", error_description: "Missing+parameter%3A+code_challenge_method"} (main.e0ffae3b.js, line 2)
[Log] Keycloak event: – "onInitError" – {error: "invalid_request", error_description: "Missing+parameter%3A+code_challenge_method"} (main.e0ffae3b.js, line 2)
   */
  pkceMethod: "S256"
};

const keycloak = new Keycloak(keycloakConfig);

const App: React.FC = () => {
  return (
    <ReactKeycloakProvider
      authClient={keycloak}
      initOptions={keycloakInitOptions}
      // чтобы видеть детали для отладки
      onEvent={(event, error) => console.log('Keycloak event:', event, error)}
      onTokens={(tokens) => console.log('Keycloak tokens:', tokens)}
    >
      <div className="App">
        <ReportPage />
      </div>
    </ReactKeycloakProvider>
  );
};

export default App;
