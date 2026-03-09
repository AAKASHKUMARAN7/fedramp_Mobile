# PRIVACY IMPACT ASSESSMENT (PIA)

## Vivo T2X 5G Mobile Information System (VTMIS)

---

| **Document Information** | |
|---|---|
| **System Name** | Vivo T2X 5G Mobile Information System (VTMIS) |
| **System Identifier** | VTMIS-2026-001 |
| **Document Version** | 1.0 |
| **Date** | March 9, 2026 |
| **Prepared By** | AAKASH |
| **Privacy Officer** | AAKASH (designated) |
| **Regulatory Basis** | E-Government Act of 2002, Section 208; OMB Circular A-130; NIST SP 800-122 |

---

## 1. SYSTEM DESCRIPTION

### 1.1 System Overview

The Vivo T2X 5G Mobile Information System (VTMIS) is a mobile device platform based on a Vivo T2X 5G smartphone (Model V2312) running Android 15 with FuntouchOS 15. The system processes, stores, and transmits information including Personally Identifiable Information (PII) in support of organizational operations for AAKASH.

### 1.2 System Components Handling PII

| Component | PII Role | Data Types |
|---|---|---|
| Android OS (API 35) | Platform for all PII processing | Account data, device identifiers |
| Contacts Database | Storage of personal contacts | Names, phone numbers, email addresses |
| SMS/MMS Application | Communication containing PII | Names, phone numbers, message content |
| Phone Dialer | Voice communication logs | Phone numbers, call duration, timestamps |
| Google Account Services | Cloud sync and authentication | Email, name, account credentials |
| DigiLocker (`org.digilocker.app2`) | Government document storage | Aadhaar, PAN, driving license, voter ID |
| SBI YONO (`com.sbi.lotusintouch`) | Banking application | Name, account number, financial data |
| WhatsApp (`com.whatsapp`) | Messaging with contacts | Names, phone numbers, messages, media |
| Telegram (`org.telegram.messenger`) | Messaging with contacts | Names, phone numbers, messages, media |
| Camera (`com.android.camera`) | Photo/video capture | Images with geolocation metadata (EXIF) |
| Chrome Browser (`com.android.chrome`) | Web browsing | Browsing history, saved passwords, autofill |
| Jio Applications (`com.jio.*`) | Carrier services | Phone number, usage data, location |

### 1.3 Operator and Users

| Role | Individual | Access to PII |
|---|---|---|
| System Owner | AAKASH | Full access (all PII on device) |
| Primary User | AAKASH (User 0) | Full access |
| Google Services | Google LLC | Synced/backed up PII |
| Carrier (Jio) | Reliance Jio | Communication metadata, location |
| App Developers | Various | Per app permission grants |

---

## 2. PII INVENTORY

### 2.1 Categories of PII Collected, Stored, or Transmitted

| # | PII Category | Sensitivity | Source | Storage Location | Retention |
|---|---|---|---|---|---|
| PII-001 | Full Name | Moderate | User input | Contacts, accounts | Indefinite |
| PII-002 | Phone Numbers | Moderate | User input, calls | Contacts, call logs | Indefinite |
| PII-003 | Email Addresses | Moderate | User input | Contacts, accounts | Indefinite |
| PII-004 | Physical Address | Moderate | User input | Contacts | Indefinite |
| PII-005 | Date of Birth | Moderate | App profiles | App databases | Per app policy |
| PII-006 | Government IDs (Aadhaar, PAN) | High | DigiLocker | DigiLocker encrypted storage | User-controlled |
| PII-007 | Financial Account Data | High | SBI YONO | App encrypted storage | Per session |
| PII-008 | Biometric Data (fingerprint) | High | Enrollment | TEE (Trustonic) | Until reset |
| PII-009 | Location Data (GPS) | High | GPS sensor | Various apps | App-specific |
| PII-010 | Photographs/Videos | Moderate | Camera | /sdcard/DCIM | User-controlled |
| PII-011 | Communication Content | High | SMS, messaging apps | App databases | Indefinite |
| PII-012 | Browsing History | Moderate | Chrome | Chrome profile | User-controlled |
| PII-013 | Device Identifiers (IMEI, serial) | Moderate | Hardware | System partition | Permanent |
| PII-014 | Authentication Credentials | High | User input | Android Keystore (TEE) | Until changed |
| PII-015 | Health Data | High (if present) | Health apps | App storage | App-specific |
| PII-016 | Social Media Profiles | Low | Instagram, Twitter(X) | App databases | Per session |

### 2.2 Volume Estimate

| Metric | Estimate |
|---|---|
| Total contacts stored | ~100-500 (estimated) |
| Total messages stored | ~1,000-10,000 |
| Total photos/videos | ~100-1,000 |
| Government documents in DigiLocker | ~1-10 |
| Financial accounts linked | ~1-3 |
| Total PII records (approximate) | ~2,000-15,000 |

---

## 3. PRIVACY ANALYSIS

### 3.1 Authority to Collect

| PII Category | Legal/Policy Authority | Justification |
|---|---|---|
| Personal contacts | User consent (personal use) | Communication needs |
| Government IDs | DigiLocker program (MeitY, GoI) | Digital document access |
| Financial data | SBI account agreement | Banking operations |
| Biometrics | Device security (user enrollment) | Authentication |
| Location data | App-level consent | Navigation, services |
| Communication content | Telecommunications regulations | Personal communication |

### 3.2 Purpose of Collection

PII is collected and stored on the VTMIS for the following purposes:

1. **Personal Communication** — Contacts, messages, call logs for personal and professional communication
2. **Government Services** — Digital access to government-issued documents via DigiLocker
3. **Financial Services** — Mobile banking through SBI YONO
4. **Authentication** — Biometric and credential data for device and app security
5. **Navigation/Location** — Maps and location-based services
6. **Federal Mission Support** — Processing Federal data (planned, requires ATO)

### 3.3 Sharing and Disclosure

| Recipient | Data Shared | Purpose | Method | Legal Basis |
|---|---|---|---|---|
| Google LLC | Account data, backups, telemetry | Cloud services, sync | Encrypted API | Terms of Service |
| Reliance Jio | IMEI, phone number, metadata | Carrier services | Cellular protocols | Service agreement |
| Meta (WhatsApp) | Phone number, contacts (hashed) | Messaging service | E2E encrypted | ToS, user consent |
| Telegram | Phone number | Messaging service | MTProto encrypted | ToS, user consent |
| SBI | Account credentials | Banking | TLS encrypted | Account agreement |
| DigiLocker/NHA | Identity verification | Document access | TLS encrypted | Government program |
| App developers (38 apps) | Per permission grants | App functionality | Various | User consent per app |
| Law enforcement | Per lawful request | Legal compliance | Per process | Court order/warrant |

### 3.4 Notice and Consent

| Notice Mechanism | Description |
|---|---|
| Android Permission System | Runtime permissions requested per app for camera, location, contacts, storage, etc. |
| App Privacy Policies | Each app provides privacy policy (ToS acceptance required) |
| Google Privacy Dashboard | Centralized view of Google data collection |
| FuntouchOS Privacy Settings | Vivo-specific privacy controls and notices |
| This PIA | Documents all PII handling for VTMIS |

### 3.5 Individual Access and Redress

| Right | Implementation |
|---|---|
| Access own PII | User has full device access (User 0, admin) |
| Correct PII | User can edit contacts, profiles, app data |
| Delete PII | User can delete data, factory reset available |
| Export PII | Google Takeout, manual export via ADB |
| Restrict processing | Per-app permission revocation |
| Data portability | Android backup, app-specific export |

---

## 4. PRIVACY RISK ASSESSMENT

### 4.1 Risk Analysis

| Risk ID | Risk Description | Likelihood | Impact | Risk Level | Mitigation |
|---|---|---|---|---|---|
| PR-001 | Unauthorized access to PII via stolen/lost device | Moderate | High | **HIGH** | Device encryption (File-Based), PIN lock, remote wipe capability |
| PR-002 | PII exposure via USB Debugging | High | High | **CRITICAL** | Disable USB debugging when not in assessment (POA&M-HIGH-001) |
| PR-003 | PII leakage via unencrypted cloud backup | Low | Moderate | **MODERATE** | Google backup uses encryption; verify settings |
| PR-004 | Location tracking by apps | Moderate | Moderate | **MODERATE** | Review and restrict location permissions |
| PR-005 | Government ID exposure via DigiLocker compromise | Low | High | **MODERATE** | DigiLocker uses server-side storage with PIN/biometric |
| PR-006 | Financial data exposure via banking app | Low | High | **MODERATE** | SBI YONO session-based, no persistent credential storage |
| PR-007 | PII in photographs (EXIF metadata) | Moderate | Low | **LOW** | Strip EXIF before sharing; review camera settings |
| PR-008 | Communication interception (no VPN) | Moderate | High | **HIGH** | Implement VPN (POA&M-CRIT-002) |
| PR-009 | Third-party app PII collection | Moderate | Moderate | **MODERATE** | Review app permissions; remove unnecessary apps |
| PR-010 | Carrier metadata collection | High | Low | **MODERATE** | VPN to limit metadata; carrier agreement accepted |
| PR-011 | Biometric data compromise | Low | High | **MODERATE** | Stored in TEE (Trustonic Level 41); hardware-isolated |
| PR-012 | PII residue after app deletion | Moderate | Low | **LOW** | Factory reset for complete PII removal |
| PR-013 | Cross-app PII sharing via Android intents | Moderate | Moderate | **MODERATE** | Work Profile to isolate Federal apps (planned) |

### 4.2 Risk Summary

| Risk Level | Count | Percentage |
|---|---|---|
| Critical | 1 | 7.7% |
| High | 2 | 15.4% |
| Moderate | 7 | 53.8% |
| Low | 3 | 23.1% |
| **Total** | **13** | **100%** |

---

## 5. PRIVACY CONTROLS (NIST SP 800-53 Rev. 5, Appendix J)

### 5.1 Authority and Purpose (AP)

| Control | Implementation |
|---|---|
| AP-1: Authority to Collect | PII collection authorized by user consent, app permissions, carrier agreement, and government program enrollment (DigiLocker) |
| AP-2: Purpose Specification | PII used for personal communication, government services, banking, authentication, and (planned) Federal mission support |

### 5.2 Accountability, Audit, and Risk Management (AR)

| Control | Implementation |
|---|---|
| AR-1: Governance and Privacy Program | AAKASH serves as privacy officer; this PIA establishes privacy governance |
| AR-2: Privacy Impact and Risk Assessment | This document (PIA); 13 privacy risks identified and mitigated |
| AR-3: Privacy Requirements for Contractors | N/A (single user, no contractors) |
| AR-4: Privacy Monitoring and Auditing | Monthly review of app permissions; quarterly review of shared data |
| AR-5: Privacy Awareness and Training | AAKASH self-trains on privacy requirements annually |

### 5.3 Data Quality and Integrity (DI)

| Control | Implementation |
|---|---|
| DI-1: Data Quality | User maintains accuracy of contacts and profiles |
| DI-2: Data Integrity and Integrity Board | File-Based Encryption, Android Verified Boot, SELinux enforce data integrity |

### 5.4 Data Minimization and Retention (DM)

| Control | Implementation |
|---|---|
| DM-1: Minimization of PII | Only necessary PII collected; app permissions restricted to minimum needed |
| DM-2: Data Retention and Disposal | Contacts/messages retained per user need; factory reset for disposal |
| DM-3: Minimization of PII in Testing | ADB assessment does not extract PII content; only system metadata collected |

### 5.5 Individual Participation and Redress (IP)

| Control | Implementation |
|---|---|
| IP-1: Consent | Android permission system provides granular consent |
| IP-2: Individual Access | User has full access to all PII on device |
| IP-3: Redress | User can modify/delete any PII directly |
| IP-4: Complaint Management | N/A (single user system) |

### 5.6 Security (SE)

| Control | Implementation |
|---|---|
| SE-1: Inventory of PII | Section 2 of this document; 16 PII categories identified |
| SE-2: Privacy Incident Response | Incident Response Plan (Document 06) covers PII breach procedures |

### 5.7 Transparency (TR)

| Control | Implementation |
|---|---|
| TR-1: Privacy Notice | This PIA serves as the privacy notice; Android permission dialogs provide runtime notice |
| TR-2: System of Records Notice | N/A (not a Federal system of records) |
| TR-3: Dissemination of Privacy Program Info | This document publicly documents privacy practices |

### 5.8 Use Limitation (UL)

| Control | Implementation |
|---|---|
| UL-1: Internal Use | PII used only for stated purposes (Section 3.2) |
| UL-2: Information Sharing with Third Parties | Shared only per Section 3.3; no unauthorized sharing |

---

## 6. SENSITIVE PII HANDLING — HIGH-SENSITIVITY ITEMS

### 6.1 Government Identity Documents (DigiLocker)

| Attribute | Detail |
|---|---|
| **App** | DigiLocker (`org.digilocker.app2`) |
| **Data Types** | Aadhaar card, PAN card, driving license, voter ID, educational certificates |
| **Storage** | Server-side (DigiLocker cloud), cached locally in encrypted app sandbox |
| **Encryption** | File-Based Encryption (Android), app-level encryption |
| **Access Control** | App-level PIN/biometric + device PIN |
| **Risk Rating** | HIGH |
| **Special Handling** | Never share via unsecured channels; never screenshot for transmission; verify app authenticity before each use |

### 6.2 Financial Data (SBI YONO)

| Attribute | Detail |
|---|---|
| **App** | SBI YONO (`com.sbi.lotusintouch`) |
| **Data Types** | Account number, balance, transaction history, UPI credentials |
| **Storage** | Session-based (minimal local storage), encrypted app sandbox |
| **Encryption** | File-Based Encryption + app-level TLS pinning |
| **Access Control** | MPIN/biometric + device PIN |
| **Risk Rating** | HIGH |
| **Special Handling** | Session timeout enforced; no credential caching; transaction alerts enabled |

### 6.3 Biometric Data

| Attribute | Detail |
|---|---|
| **Storage Location** | Trusted Execution Environment (Trustonic TEE, Level 41) |
| **Data Types** | Fingerprint templates (not raw images) |
| **Encryption** | Hardware-isolated, not accessible to Android OS |
| **Access Control** | Hardware keystore; cannot be extracted |
| **Risk Rating** | HIGH (but well-mitigated by hardware isolation) |
| **Special Handling** | Biometric data never leaves TEE; factory reset destroys enrollment |

---

## 7. DATA FLOW — PII LIFECYCLE

### 7.1 Collection
```
User Input ──→ Android App ──→ App Sandbox (encrypted)
Sensor Data ──→ Android OS ──→ Permission-gated API
Network Data ──→ App via TLS ──→ App Sandbox
```

### 7.2 Processing
```
App Sandbox ──→ App Runtime ──→ Display to User
                    │
                    ├──→ Local computation (no PII export)
                    └──→ API calls (TLS encrypted)
```

### 7.3 Storage
```
App Sandbox (File-Based Encryption)
    ├── Contacts DB
    ├── Messages DB
    ├── Media files
    ├── App-specific databases
    └── Credentials ──→ Android Keystore (TEE)
```

### 7.4 Transmission
```
App ──→ TLS 1.2+ ──→ Jio Network ──→ Internet ──→ Service Provider
                           │
                      [No VPN — PII exposure risk]
                           │
                      [VPN planned — POA&M-CRIT-002]
```

### 7.5 Disposal
```
Individual items: App delete function
App data: Settings > Apps > Clear Data
Complete: Settings > System > Factory Reset
Secure: Factory reset + overwrite (ADB fill storage)
```

---

## 8. PIA DETERMINATION

### 8.1 Privacy Threshold Analysis

| Question | Answer |
|---|---|
| Does the system collect PII? | **YES** — 16 categories identified |
| Is the PII about members of the public? | **YES** — Contacts include non-organizational individuals |
| Is a System of Records Notice required? | **NO** — Not a Federal system of records |
| Does the system collect sensitive PII? | **YES** — Government IDs, financial data, biometrics |
| Is PII shared externally? | **YES** — With Google, carrier, app developers |
| Is a full PIA required? | **YES** |

### 8.2 PIA Approval

| Role | Name | Signature | Date |
|---|---|---|---|
| System Owner | AAKASH | _________________ | __________ |
| Privacy Officer | AAKASH (designated) | _________________ | __________ |
| Authorizing Official | [TBD] | _________________ | __________ |

---

## 9. RECOMMENDATIONS

1. **CRITICAL** — Disable USB Debugging to prevent PII extraction (PR-002)
2. **HIGH** — Implement VPN to encrypt PII in transit (PR-008)
3. **HIGH** — Enable remote wipe capability (Google Find My Device) to protect PII on lost device (PR-001)
4. **MODERATE** — Review and minimize app permissions quarterly (PR-009)
5. **MODERATE** — Strip EXIF metadata from photos before sharing (PR-007)
6. **MODERATE** — Implement Android Work Profile to isolate Federal PII from personal data (PR-013)
7. **LOW** — Document PII retention schedule and enforce periodic purging (PR-012)

---

## 10. REVIEW SCHEDULE

| Activity | Frequency |
|---|---|
| Full PIA review | Annually |
| PII inventory update | Semi-annually |
| Privacy risk re-assessment | Annually or upon significant change |
| App permission audit | Quarterly |
| Privacy incident review | After any incident |

---

**Document Control**

| Version | Date | Author | Changes |
|---|---|---|---|
| 1.0 | March 9, 2026 | AAKASH | Initial Privacy Impact Assessment |

---

*END OF PRIVACY IMPACT ASSESSMENT*
