/*
 * Testerra
 *
 * (C) 2020,  Peter Lehmann, T-Systems Multimedia Solutions GmbH, Deutsche Telekom AG
 *
 * Deutsche Telekom AG and all other contributors /
 * copyright owners license this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */
 package eu.tsystems.mms.tic.testframework.utils;

import eu.tsystems.mms.tic.testframework.common.PropertyManager;
import eu.tsystems.mms.tic.testframework.exceptions.SystemException;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509ExtendedTrustManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CertUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CertUtils.class);

    private String[] trustedHosts;
    private boolean trustAllHosts = false;

    public CertUtils() {
        String trustHostsProperty = PropertyManager.getProperty("tt.cert.trusted.hosts","").trim();
        if (!trustHostsProperty.isEmpty()) {
            if (trustHostsProperty.equals("*")) {
                setTrustAllHosts(true);
            } else {
                setTrustedHosts(trustHostsProperty.split("\\s+"));
            }
        }
    }

    /**
     * The Constant ALL_TRUSTING_TRUST_MANAGER.
     */
    private static final TrustManager[] ALL_TRUSTING_TRUST_MANAGER = new TrustManager[]{
            new X509ExtendedTrustManager() {

                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {
                }

                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }

            }
    };

    public HostnameVerifier getHostnameVerifier() {
        if (trustAllHosts) {
            return (hostname, sslSession) -> true;
        } else {
            return (hostname, sslSession) -> Arrays.stream(trustedHosts).anyMatch(trustedHostname -> trustedHostname.equals(hostname));
        }

    }

    /**
     * Sets a new DefaultSSLSocketFactory which accepts all certs. ALso it sets a new DefaultHostnameVerifier which
     * accepts all host names.
     */
    public static void trustAllCerts() {
        // Install the all-trusting trust manager
        SSLContext sc = null;
        String msg = "Unable to create socket factory";
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, ALL_TRUSTING_TRUST_MANAGER, new java.security.SecureRandom());
        } catch (Exception e) {
            LOGGER.error(msg, e);
        }
        if (sc != null) {
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        }

        CertUtils certUtils = new CertUtils();
        certUtils.setTrustAllHosts(true);

        // set default hostname verifier for https
        HttpsURLConnection.setDefaultHostnameVerifier(certUtils.getHostnameVerifier());
    }

    /**
     * Overrides the SSL TrustManager and HostnameVerifier to allow all certs and hostnames. WARNING: This should only
     * be used for testing, or in a "safe" (i.e. firewalled) environment.
     *
     * @param connection the new accept all verifier
     * @return HttpsURLConnection
     */
    public static HttpsURLConnection trustAllCerts(HttpsURLConnection connection, SSLSocketFactory sslSocketFactory) {

        // Create the socket factory.
        // Reusing the same socket factory allows sockets to be
        // reused, supporting persistent connections.
        if (null == sslSocketFactory) {
            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, ALL_TRUSTING_TRUST_MANAGER, new java.security.SecureRandom());
                sslSocketFactory = sc.getSocketFactory();
            } catch (NoSuchAlgorithmException e) {
                throw new SystemException("Error trusting all certificates.", e);
            } catch (KeyManagementException e) {
                throw new SystemException("Error trusting all certificates.", e);
            }
        }

        connection.setSSLSocketFactory(sslSocketFactory);

        CertUtils certUtils = new CertUtils();
        certUtils.setTrustAllHosts(true);

        // Since we may be using a cert with a different name, we need to ignore
        // the hostname as well.
        connection.setHostnameVerifier(certUtils.getHostnameVerifier());

        return connection;
    }

    public String[] getTrustedHosts() {
        return this.trustedHosts;
    }

    public boolean isTrustAllHosts() {
        return this.trustAllHosts;
    }

    public CertUtils setTrustedHosts(String[] hosts) {
        this.trustedHosts = hosts;
        return this;
    }

    public CertUtils setTrustAllHosts(boolean trustAll) {
        this.trustAllHosts = trustAll;
        return this;
    }

}
