# RULES OF BEHAVIOR (ROB)

## Vivo T2X 5G Mobile Information System (VTMIS)

---

| **Document Information** | |
|---|---|
| **System Name** | Vivo T2X 5G Mobile Information System (VTMIS) |
| **System Identifier** | VTMIS-2026-001 |
| **Document Version** | 1.0 |
| **Effective Date** | March 9, 2026 |
| **Prepared By** | AAKASH |
| **Regulatory Basis** | NIST SP 800-53 Rev. 5: PL-4 (Rules of Behavior), FISMA |

---

## 1. PURPOSE

This document establishes the Rules of Behavior (ROB) for all users of the Vivo T2X 5G Mobile Information System (VTMIS). These rules define acceptable and unacceptable activities, responsibilities, and consequences for violations. All individuals who access the VTMIS must read, understand, and acknowledge these rules before being granted access.

---

## 2. SCOPE

These rules apply to:
- **All users** of the VTMIS (currently: AAKASH, User 0)
- **All data** processed, stored, or transmitted by the device
- **All connected systems** including the tethered workstation, carrier network, cloud services, and any Federal systems
- **All locations** where the device is used (home, office, travel, public)
- **All operating modes** (normal operation, assessment, maintenance, emergency)

---

## 3. SYSTEM DESCRIPTION

The VTMIS consists of a Vivo T2X 5G smartphone (Serial: 10BDC81HTA000Z4) running Android 15 (FuntouchOS 15) with the following characteristics:

- **Processor:** MediaTek Dimensity 6020 (octa-core ARMv8.2)
- **Memory:** 6 GB RAM + 6 GB extended
- **Storage:** 128 GB (106 GB user-accessible)
- **Connectivity:** 5G NR, LTE, Wi-Fi, Bluetooth, USB
- **Security:** File-Based Encryption, SELinux Enforcing, Verified Boot, TEE (Trustonic)
- **Carrier:** Reliance Jio
- **Primary Use:** Personal and organizational information processing

---

## 4. GENERAL RULES OF BEHAVIOR

### 4.1 Device Physical Security

| Rule # | Rule | Classification |
|---|---|---|
| ROB-PHY-001 | The device must be physically secured and within the user's possession or in a locked location at all times | MANDATORY |
| ROB-PHY-002 | The device must never be left unattended in public locations (restaurants, transit, conference rooms) | MANDATORY |
| ROB-PHY-003 | The screen must be locked when not in active use (auto-lock ≤ 30 seconds required) | MANDATORY |
| ROB-PHY-004 | The device must not be handed to unauthorized individuals, even temporarily | MANDATORY |
| ROB-PHY-005 | Loss or theft of the device must be reported immediately and remote wipe initiated | MANDATORY |
| ROB-PHY-006 | The device should be stored in a protective case to prevent physical damage | RECOMMENDED |
| ROB-PHY-007 | When traveling, the device must be carried in carry-on luggage, not checked baggage | MANDATORY |

### 4.2 Authentication and Access Control

| Rule # | Rule | Classification |
|---|---|---|
| ROB-AUTH-001 | A PIN of at least 6 digits must be set as the primary unlock method | MANDATORY |
| ROB-AUTH-002 | Biometric authentication (fingerprint) may be used as a secondary unlock method | PERMITTED |
| ROB-AUTH-003 | Pattern unlock and face unlock (without IR depth) are NOT permitted | PROHIBITED |
| ROB-AUTH-004 | Smart Lock features (Trusted Places, On-Body Detection) must be disabled | MANDATORY |
| ROB-AUTH-005 | PIN/password must not be shared with any other individual | MANDATORY |
| ROB-AUTH-006 | PIN/password must be changed every 90 days or upon suspected compromise | MANDATORY |
| ROB-AUTH-007 | Failed unlock attempts must trigger wipe after 10 consecutive failures | MANDATORY |
| ROB-AUTH-008 | Lock screen notifications must not display message content ("Hide sensitive content") | MANDATORY |

### 4.3 Data Handling

| Rule # | Rule | Classification |
|---|---|---|
| ROB-DATA-001 | Federal data must be processed only through approved applications | MANDATORY |
| ROB-DATA-002 | Federal data must not be shared via personal messaging apps (WhatsApp, Telegram, Instagram) | MANDATORY |
| ROB-DATA-003 | Screenshots of Federal data must not be taken unless operationally required and approved | MANDATORY |
| ROB-DATA-004 | Federal data must not be stored on external SD cards (if applicable) | MANDATORY |
| ROB-DATA-005 | Data classification labels must be mentally applied to all information before sharing | MANDATORY |
| ROB-DATA-006 | Cloud backups containing Federal data must use approved, encrypted backup services only | MANDATORY |
| ROB-DATA-007 | Copying Federal data to clipboard must be minimized; clipboard must be cleared after use | RECOMMENDED |
| ROB-DATA-008 | Personal and Federal data should be separated using Android Work Profile when available | RECOMMENDED |

### 4.4 Network and Connectivity

| Rule # | Rule | Classification |
|---|---|---|
| ROB-NET-001 | VPN must be active when accessing Federal systems or data | MANDATORY |
| ROB-NET-002 | Public Wi-Fi networks must NOT be used for Federal data access | PROHIBITED |
| ROB-NET-003 | The device hotspot (ap0) must use WPA2/WPA3 with a strong passphrase (≥ 12 characters) | MANDATORY |
| ROB-NET-004 | Bluetooth must be set to non-discoverable when not actively pairing | MANDATORY |
| ROB-NET-005 | NFC must be disabled when not actively in use | RECOMMENDED |
| ROB-NET-006 | Wi-Fi auto-connect to known networks must be disabled | MANDATORY |
| ROB-NET-007 | Only one workstation may be tethered via hotspot at a time | MANDATORY |
| ROB-NET-008 | USB connections must only be made to authorized workstations | MANDATORY |

### 4.5 Application Management

| Rule # | Rule | Classification |
|---|---|---|
| ROB-APP-001 | Applications must only be installed from Google Play Store | MANDATORY |
| ROB-APP-002 | "Install from Unknown Sources" must remain disabled | MANDATORY |
| ROB-APP-003 | App permissions must be reviewed and minimized at installation and quarterly thereafter | MANDATORY |
| ROB-APP-004 | Applications requesting excessive permissions must be denied or removed | MANDATORY |
| ROB-APP-005 | Google Play Protect must remain enabled at all times | MANDATORY |
| ROB-APP-006 | Sideloaded APKs are prohibited unless explicitly approved by the System Owner | PROHIBITED |
| ROB-APP-007 | Applications must be kept up to date (auto-update enabled) | MANDATORY |
| ROB-APP-008 | Unused applications should be uninstalled, not just disabled | RECOMMENDED |

### 4.6 Developer Options and USB Debugging

| Rule # | Rule | Classification |
|---|---|---|
| ROB-DEV-001 | Developer Options must be disabled during normal operations | MANDATORY |
| ROB-DEV-002 | USB Debugging must be disabled except during authorized security assessments | MANDATORY |
| ROB-DEV-003 | When USB Debugging is enabled, the device must be physically supervised at all times | MANDATORY |
| ROB-DEV-004 | ADB RSA fingerprint authorization must always be verified before accepting | MANDATORY |
| ROB-DEV-005 | "Revoke USB debugging authorizations" must be executed after each assessment session | MANDATORY |
| ROB-DEV-006 | OEM Unlock must remain disabled (bootloader locked) | MANDATORY |

### 4.7 Security Updates and Maintenance

| Rule # | Rule | Classification |
|---|---|---|
| ROB-MAINT-001 | Security patches must be applied within 30 days of release (Critical: 7 days) | MANDATORY |
| ROB-MAINT-002 | OS updates must be applied within 30 days of availability after compatibility verification | MANDATORY |
| ROB-MAINT-003 | App updates must be applied within 14 days (auto-update preferred) | MANDATORY |
| ROB-MAINT-004 | Device storage must not exceed 90% utilization (currently 97% — requires cleanup) | MANDATORY |
| ROB-MAINT-005 | Local backups must be performed weekly via ADB or Google Cloud | MANDATORY |
| ROB-MAINT-006 | Battery health must be monitored; device must not operate below 10% charge | RECOMMENDED |

---

## 5. PROHIBITED ACTIVITIES

The following activities are strictly **PROHIBITED** on the VTMIS:

| # | Prohibited Activity | Rationale |
|---|---|---|
| PROH-001 | Rooting the device or unlocking the bootloader | Destroys Verified Boot chain, voids warranty, defeats security |
| PROH-002 | Installing custom ROMs or kernels | Removes manufacturer security features, introduces unknown code |
| PROH-003 | Using the device to access, store, or transmit classified information | VTMIS is not authorized for classified data |
| PROH-004 | Connecting to unauthorized networks for Federal data access | Risk of interception and MITM attacks |
| PROH-005 | Sharing device credentials with any individual | Single-user authorization only |
| PROH-006 | Disabling device encryption | Encryption is mandatory for FedRAMP compliance |
| PROH-007 | Using the device for illegal activities | Legal compliance required |
| PROH-008 | Bypassing or tampering with SELinux policy | SELinux Enforcing mode is a critical security control |
| PROH-009 | Using the device while driving or operating machinery | Safety requirement |
| PROH-010 | Photographing or recording classified/restricted areas with device camera | Operational security |
| PROH-011 | Storing Federal passwords in browser autofill or unencrypted notes | Credential compromise risk |
| PROH-012 | Enabling USB Debugging for non-assessment purposes | PII extraction risk (SAR-HIGH-001) |

---

## 6. INCIDENT REPORTING REQUIREMENTS

All users must report the following events **immediately** (within 1 hour):

| Event Type | Reporting Action |
|---|---|
| Device lost or stolen | Initiate remote wipe; report to System Owner |
| Suspected malware infection | Disconnect from network; run Play Protect scan; report |
| Unauthorized access attempt | Change credentials; report |
| Suspicious app behavior | Remove app; report |
| Accidental PII/Federal data exposure | Document details; report per IRP |
| Physical damage affecting security | Assess impact; report if security compromised |
| Unusual battery drain or heat | May indicate malware; investigate and report |

**Reporting Contact:** AAKASH (System Owner / ISSM)

---

## 7. CONSEQUENCES OF VIOLATIONS

### 7.1 Violation Categories

| Severity | Examples | Consequences |
|---|---|---|
| **Critical** | Rooting device, disabling encryption, sharing credentials, accessing classified data | Immediate access revocation, device lockdown, incident report |
| **Major** | Using public Wi-Fi for Federal data, disabling SELinux, installing unknown APKs | Access suspension, mandatory retraining, documented warning |
| **Minor** | Exceeding storage threshold, delayed patch application, not locking screen | Documented counseling, corrective action plan |
| **Administrative** | Not completing annual training, not reviewing permissions quarterly | Reminder and timeline for compliance |

### 7.2 Enforcement Process

1. **Detection** — Violation identified through monitoring, audit, or self-report
2. **Documentation** — Violation documented with date, time, nature, and evidence
3. **Assessment** — Severity determined per categories above
4. **Response** — Appropriate consequence applied
5. **Remediation** — Corrective actions implemented
6. **Follow-up** — Verification that corrective actions are effective

---

## 8. USER RESPONSIBILITIES

### 8.1 Before First Use
- [ ] Read and understand this Rules of Behavior document completely
- [ ] Sign the acknowledgment form (Section 10)
- [ ] Complete security awareness training
- [ ] Verify device encryption is active
- [ ] Set compliant PIN (≥ 6 digits)
- [ ] Enable Google Find My Device for remote wipe capability
- [ ] Disable Developer Options and USB Debugging

### 8.2 Ongoing Responsibilities
- [ ] Maintain physical control of the device
- [ ] Apply security updates promptly
- [ ] Review app permissions quarterly
- [ ] Monitor storage utilization (keep below 90%)
- [ ] Perform weekly backups
- [ ] Report security incidents immediately
- [ ] Re-acknowledge ROB annually

### 8.3 Upon Role Change or Departure
- [ ] Export required Federal data per approved procedures
- [ ] Factory reset device (or remove Federal data/apps if personal device)
- [ ] Return any government-provided accessories
- [ ] Confirm data removal with System Owner
- [ ] Revoke all Federal system access

---

## 9. SPECIAL CIRCUMSTANCES

### 9.1 Travel (Domestic)

| Requirement | Detail |
|---|---|
| Device must be carried on-person or in carry-on | Never in checked luggage |
| VPN must be active on hotel/airport Wi-Fi | Or use cellular data only |
| Device must not be left in hotel safe (non-secured) | Carry at all times |
| Hotspot usage for workstation must use WPA3 if available | WPA2 minimum |

### 9.2 Travel (International)

| Requirement | Detail |
|---|---|
| All applicable domestic rules apply | Plus additional below |
| Device may be subject to border inspection | Minimize Federal data before travel |
| Temporary travel PIN may be used | Change back upon return |
| Notify System Owner before international travel | For risk assessment |
| Consider using a travel-only device | Leave VTMIS at home if possible |

### 9.3 Maintenance and Assessment Mode

| Requirement | Detail |
|---|---|
| USB Debugging may be enabled only for authorized assessments | Must be supervised |
| Developer Options may be enabled temporarily | Disable after completion |
| ADB connections only from authorized workstations | Verify RSA key |
| Assessment activities must be logged | Date, time, commands, assessor |

---

## 10. ACKNOWLEDGMENT OF RULES OF BEHAVIOR

### User Acknowledgment Form

---

**I, the undersigned, acknowledge that:**

1. I have received, read, and understand the Rules of Behavior for the Vivo T2X 5G Mobile Information System (VTMIS-2026-001).

2. I understand my responsibilities for protecting the information system and the data it processes, stores, and transmits.

3. I understand that violations of these rules may result in consequences including access revocation, device lockdown, and incident reporting.

4. I agree to comply with all rules designated as MANDATORY and to follow RECOMMENDED practices to the extent practical.

5. I understand that these rules may be updated and that I am responsible for reviewing and acknowledging updates.

6. I understand that my activities on the system may be monitored and audited.

7. I will report any security incidents, policy violations, or suspicious activities immediately.

---

| Field | Entry |
|---|---|
| **Printed Name** | AAKASH |
| **Signature** | _________________________________ |
| **Date** | _________________________________ |
| **System Name** | Vivo T2X 5G Mobile Information System (VTMIS) |
| **Role** | System Owner / Primary User |
| **Acknowledgment Valid Until** | One year from signature date |

---

| **Witnessed By** | |
|---|---|
| **Printed Name** | _________________________________ |
| **Title** | _________________________________ |
| **Signature** | _________________________________ |
| **Date** | _________________________________ |

---

## 11. REVIEW AND UPDATE HISTORY

| Activity | Frequency | Responsible |
|---|---|---|
| Full ROB review | Annually | AAKASH |
| ROB update for policy changes | As needed | AAKASH |
| User re-acknowledgment | Annually | All users |
| New user acknowledgment | Before first access | New users |

---

**Document Control**

| Version | Date | Author | Changes |
|---|---|---|---|
| 1.0 | March 9, 2026 | AAKASH | Initial Rules of Behavior |

---

*END OF RULES OF BEHAVIOR*
