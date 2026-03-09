# ACCESS CONTROL POLICY

## Vivo T2X 5G Mobile Information System (VTMIS)

---

| **Document Information** | |
|---|---|
| **System Name** | Vivo T2X 5G Mobile Information System (VTMIS) |
| **System Identifier** | VTMIS-2026-001 |
| **Document Version** | 1.0 |
| **Date** | March 9, 2026 |
| **Prepared By** | AAKASH |

---

## 1. PURPOSE

This Access Control Policy establishes the rules, responsibilities, and procedures for controlling physical and logical access to the VTMIS and the Federal information it processes. This policy is developed in accordance with NIST SP 800-53 Rev. 5, Access Control (AC) family.

---

## 2. SCOPE

This policy applies to:
- The Vivo T2X 5G device (V2312, Serial: 10BDC81HTA000Z4)
- All user accounts on the device (currently: User 0 — Aakash)
- All applications installed on the device
- All network interfaces (Cellular, Wi-Fi, Bluetooth, USB)
- The tethered Windows 11 workstation
- All Federal information processed, stored, or transmitted by the system

---

## 3. AUTHORIZED USERS

### 3.1 User Accounts

| User ID | User Name | Role | Access Level | Authentication |
|---|---|---|---|---|
| User 0 | Aakash | System Owner / ISSO / User | Full (Owner) | PIN + Biometric |

### 3.2 No Additional Users Authorized

- No additional user profiles shall be created on the device
- Guest mode shall remain disabled
- No shared device usage is permitted
- The device is classified as a **single-user Federal information system**

---

## 4. AUTHENTICATION REQUIREMENTS

### 4.1 Primary Authentication

| Requirement | Policy | Current Status |
|---|---|---|
| **Lock Screen Type** | PIN minimum; Complex password recommended | PIN (Compliant) |
| **PIN Length** | Minimum 6 digits | [To be verified] |
| **Password Complexity** | If password: min 8 chars, mixed case, numbers, special | N/A (PIN) |
| **Auto-Lock Timeout** | Maximum 5 minutes of inactivity | [To be configured] |
| **Authentication on Boot** | Required before any data access | ✅ (FBE requires) |
| **PIN Change Frequency** | Every 90 days | Last changed: 2026-02-26 |

### 4.2 Biometric Authentication (Convenience Factor)

| Biometric | Status | Policy |
|---|---|---|
| **Fingerprint** | Available (android.hardware.fingerprint) | Permitted as secondary factor after initial PIN |
| **Face Recognition** | Available (android.hardware.biometrics.face, Vivo FaceUI) | Permitted as secondary factor after initial PIN |

**Biometric Policy:**
- Biometrics supplement but do not replace PIN/password
- PIN re-authentication required every 72 hours even with biometric use
- Biometric templates stored in Trustonic TEE (hardware-protected)
- Face recognition should use enhanced (3D) mode if available

### 4.3 Failed Authentication Lockout

| Threshold | Action |
|---|---|
| 5 consecutive failures | 30-second lockout |
| 10 consecutive failures | Progressive lockout (escalating delays) |
| 20+ consecutive failures | Require Google account verification or factory reset |

---

## 5. ACCESS CONTROL RULES

### 5.1 Physical Access

| Rule | Description |
|---|---|
| **PA-01** | Device must be in personal possession of AAKASH at all times |
| **PA-02** | Device shall not be left unattended in unsecured areas |
| **PA-03** | Device screen must lock when not in active use (auto-lock ≤ 5 min) |
| **PA-04** | USB port access: Only authorized cables/chargers |
| **PA-05** | SIM tray: Not removable without PIN (SIM lock recommended) |

### 5.2 Logical Access

| Rule | Description |
|---|---|
| **LA-01** | All access requires successful authentication (PIN/biometric) |
| **LA-02** | SELinux must remain in Enforcing mode at all times |
| **LA-03** | Developer Options must remain disabled except during authorized maintenance |
| **LA-04** | USB Debugging must remain disabled except during authorized assessment |
| **LA-05** | No root access — bootloader must remain locked |
| **LA-06** | Applications must request and be granted minimum necessary permissions |

### 5.3 Network Access

| Rule | Description |
|---|---|
| **NA-01** | Federal data transmission requires VPN (when configured) |
| **NA-02** | Wi-Fi hotspot must use WPA2/WPA3 with strong passphrase (min 12 chars) |
| **NA-03** | Maximum 16 hotspot clients (system limit) — only authorized workstation |
| **NA-04** | Bluetooth pairing only with known, authorized devices |
| **NA-05** | No connection to unknown/untrusted Wi-Fi networks |
| **NA-06** | Wi-Fi MAC randomization must be enabled |

### 5.4 Application Access

| Rule | Description |
|---|---|
| **AA-01** | Applications installed only from Google Play Store |
| **AA-02** | No sideloading (Unknown Sources must be disabled) |
| **AA-03** | App permissions reviewed and minimized |
| **AA-04** | AI/LLM apps (ChatGPT, Gemini) prohibited for Federal data |
| **AA-05** | Social media apps prohibited for Federal data sharing |
| **AA-06** | Work Profile recommended for Federal data isolation |

### 5.5 Data Access

| Rule | Description |
|---|---|
| **DA-01** | Federal data shall be stored in encrypted storage only |
| **DA-02** | Federal data shall not be shared via social media, messaging (unless E2E encrypted and authorized), or file transfer apps |
| **DA-03** | Federal data shall not be processed by third-party AI services |
| **DA-04** | Federal data shall not be backed up to unauthorized cloud services |
| **DA-05** | Federal data clipboard contents cleared after use |

---

## 6. USB ACCESS CONTROL

### 6.1 USB Connection Policy

| USB Mode | Policy | Risk |
|---|---|---|
| **Charging Only** | Permitted at all times | Minimal |
| **MTP (File Transfer)** | Permitted only with owned devices | Moderate |
| **PTP (Photo Transfer)** | Permitted only with owned devices | Low |
| **ADB (Debugging)** | Prohibited except during authorized assessment | High |
| **MIDI** | Not applicable | N/A |
| **USB Tethering** | Permitted for authorized workstation only | Moderate |

### 6.2 Current USB State

- **Configured:** `persist.sys.usb.config = adb` (**NON-COMPLIANT** — should be `mtp`)
- **Active:** `sys.usb.state = mtp,adb` (**NON-COMPLIANT** — should be `mtp`)
- **Required Action:** Disable USB Debugging after assessment

---

## 7. REMOTE ACCESS CONTROL

| Method | Status | Policy |
|---|---|---|
| ADB over USB | Enabled (assessment only) | Must be disabled post-assessment |
| ADB over Wi-Fi | Disabled | Strictly prohibited |
| Google Find My Device | Enabled | Authorized for emergency lock/wipe |
| Remote Desktop | Not installed | Not authorized |
| TeamViewer/AnyDesk | Not installed | Not authorized |

---

## 8. SEPARATION OF DUTIES

While VTMIS is a single-user system, logical separation is maintained:

| Function | Role | Enforcement |
|---|---|---|
| System Administration | AAKASH (as Admin) | Requires Developer Mode (normally disabled) |
| Regular Usage | AAKASH (as User) | Standard user permissions |
| Security Assessment | AAKASH (as ISSO) | Requires ADB session |
| Change Approval | AAKASH (as Change Authority) | Change request documentation |

---

## 9. ACCESS REVIEW

| Review Activity | Frequency | Details |
|---|---|---|
| User account review | Quarterly | Verify only authorized user exists |
| Application permission audit | Monthly | Review all app permissions |
| Paired Bluetooth devices | Monthly | Remove unauthorized pairings |
| Wi-Fi hotspot clients | Weekly | Verify only authorized workstation |
| Access control policy review | Annually | Update this document |

---

## 10. POLICY VIOLATIONS

| Violation | Severity | Response |
|---|---|---|
| Unauthorized user account created | High | Immediate removal, incident report |
| Federal data shared via unauthorized app | High | Incident response activation |
| Device left unsecured and unauthorized access occurs | Critical | Incident response, potential remote wipe |
| Sideloaded application installed | Moderate | Remove app, review for malware |
| USB Debugging left enabled | Moderate | Disable immediately, document |

---

**Document Control**

| Version | Date | Author | Changes |
|---|---|---|---|
| 1.0 | March 9, 2026 | AAKASH | Initial Access Control Policy |

---

*END OF ACCESS CONTROL POLICY*
