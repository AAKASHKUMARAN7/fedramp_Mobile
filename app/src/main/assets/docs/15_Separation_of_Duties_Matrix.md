# SEPARATION OF DUTIES MATRIX

## Vivo T2X 5G Mobile Information System (VTMIS)

---

| **Document Information** | |
|---|---|
| **System Name** | Vivo T2X 5G Mobile Information System (VTMIS) |
| **System Identifier** | VTMIS-2026-001 |
| **Document Version** | 1.0 |
| **Date** | March 9, 2026 |
| **Prepared By** | AAKASH |
| **Regulatory Basis** | NIST SP 800-53 Rev. 5: AC-5 (Separation of Duties) |

---

## 1. PURPOSE

This document defines the separation of duties for the Vivo T2X 5G Mobile Information System (VTMIS). As a single-user mobile device system, traditional multi-person role separation is inherently limited. This document addresses compensating controls and logical role separation to satisfy FedRAMP AC-5 requirements.

---

## 2. ROLES AND RESPONSIBILITIES

### 2.1 Defined Roles

| Role ID | Role Title | Assigned To | Description |
|---|---|---|---|
| R-001 | **System Owner** | AAKASH | Overall authority and accountability for the system |
| R-002 | **Information System Security Manager (ISSM)** | AAKASH | Manages security posture, risk assessment, policy |
| R-003 | **System Administrator** | AAKASH | Day-to-day system administration, configuration |
| R-004 | **End User** | AAKASH | Operational use of the system for mission functions |
| R-005 | **Security Assessor** | AAKASH (with external review planned) | Conducts security assessments |
| R-006 | **Incident Responder** | AAKASH | First responder for security incidents |
| R-007 | **Auditor** | [External — TBD] | Independent review of security controls |
| R-008 | **Authorizing Official (AO)** | [Agency AO — TBD] | Grants/denies authorization to operate |

### 2.2 Role Consolidation Justification

As a personal mobile information system operated by a single individual, roles R-001 through R-006 are consolidated under AAKASH. This consolidation is:

- **Acknowledged** as a deviation from ideal separation of duties
- **Documented** as a risk in the Security Assessment Report (SAR-MOD-010)
- **Compensated** through the controls described in Section 4
- **Accepted** as inherent to the nature of a personal mobile device system

---

## 3. SEPARATION OF DUTIES MATRIX

### 3.1 Function-by-Role Matrix

| # | Function / Activity | System Owner | ISSM | Sys Admin | End User | Assessor | Auditor | AO |
|---|---|---|---|---|---|---|---|---|
| F-001 | Grant/revoke system authorization | A | R | — | — | C | I | **P** |
| F-002 | Define security policies | A | **P** | I | I | C | I | A |
| F-003 | Implement security configurations | I | A | **P** | — | V | V | — |
| F-004 | Install/remove applications | A | C | **P** | R | — | V | — |
| F-005 | Apply security patches | A | A | **P** | — | V | V | — |
| F-006 | Process Federal data | I | — | — | **P** | — | V | — |
| F-007 | Conduct security assessments | I | C | S | — | **P** | V | I |
| F-008 | Review audit logs | I | **P** | S | — | C | **P** | I |
| F-009 | Modify access controls | A | A | **P** | — | V | V | — |
| F-010 | Manage encryption keys | A | C | **P** | — | V | — | — |
| F-011 | Perform backups | I | C | **P** | — | V | V | — |
| F-012 | Respond to incidents | A | **P** | S | R | C | I | I |
| F-013 | Accept residual risk | **P** | C | — | — | C | I | A |
| F-014 | Approve configuration changes | **P** | C | — | — | — | V | — |
| F-015 | Monitor continuous compliance | I | **P** | S | — | C | **P** | I |
| F-016 | Factory reset/device wipe | **P** | C | S | — | — | I | I |
| F-017 | Enable/disable USB Debugging | A | A | **P** | — | V | V | — |
| F-018 | Manage hotspot/tethering | I | C | **P** | — | V | — | — |
| F-019 | Review POA&M items | A | **P** | — | — | C | V | **P** |
| F-020 | Update SCRM plan | A | **P** | — | — | C | V | I |

**Legend:**
- **P** = Primary (performs the function)
- **A** = Approves
- **R** = Requests
- **C** = Consulted
- **I** = Informed
- **S** = Supports
- **V** = Verifies/Validates
- **—** = No involvement

### 3.2 Conflict Matrix (Incompatible Functions)

| Conflict # | Function A | Function B | Risk | Compensating Control |
|---|---|---|---|---|
| CON-001 | F-003: Implement security configs | F-007: Assess security | Self-assessment bias | External assessment planned (R-007); automated scan tools supplement |
| CON-002 | F-004: Install apps | F-008: Review audit logs | Could install and hide evidence | Play Protect independent scanning; Google Cloud forensic trail |
| CON-003 | F-006: Process Federal data | F-009: Modify access controls | Could weaken controls for convenience | Configuration baselines documented; quarterly compliance audit |
| CON-004 | F-014: Approve changes | F-003: Implement changes | Self-approval | Change log documentation; assessment verification |
| CON-005 | F-013: Accept risk | F-007: Assess risk | Could accept own under-assessed risks | POA&M tracked; AO external review required (R-008) |
| CON-006 | F-016: Device wipe | F-008: Review logs | Could destroy evidence | Google Cloud audit trail; backup before wipe |
| CON-007 | F-017: Enable USB Debug | F-006: Process data | PII exposure during debug | ROB-DEV rules; USB Debug disabled during operations |

---

## 4. COMPENSATING CONTROLS

Since full personnel-based separation of duties is not achievable in a single-user system, the following compensating controls are implemented:

### 4.1 Technical Compensating Controls

| Control ID | Control | Implementation | Addresses |
|---|---|---|---|
| CC-001 | **Automated security scanning** | Google Play Protect performs independent malware scanning without user intervention | CON-001, CON-002 |
| CC-002 | **Verified Boot chain** | Boot integrity verified by hardware before OS loads — user cannot bypass | CON-002 |
| CC-003 | **SELinux Mandatory Access Control** | Kernel-enforced policy independent of user actions | CON-003 |
| CC-004 | **File-Based Encryption** | Encryption always active; cannot be disabled without factory reset | CON-003 |
| CC-005 | **Google Cloud audit trail** | Google account activity log independent of device | CON-006 |
| CC-006 | **Hardware Keystore (TEE)** | Cryptographic operations isolated in Trustonic TEE | CON-003 |
| CC-007 | **ADB command logging** | ADB commands logged in system log (logcat) | CON-004 |
| CC-008 | **Android permission model** | OS-enforced app sandboxing and permission gates | CON-002 |

### 4.2 Procedural Compensating Controls

| Control ID | Control | Implementation | Addresses |
|---|---|---|---|
| CC-009 | **Change documentation** | All configuration changes documented in change log with date/time/reason | CON-004 |
| CC-010 | **Pre-post configuration snapshots** | ADB property dumps before/after changes | CON-004 |
| CC-011 | **External auditor review** | Annual independent review by external party (R-007) | CON-001, CON-005 |
| CC-012 | **Authorizing Official review** | AO independently reviews risk acceptance decisions | CON-005 |
| CC-013 | **Self-assessment checklists** | Structured checklists reduce subjective assessment bias | CON-001 |
| CC-014 | **Temporal separation** | Administrative functions performed in dedicated sessions, not during normal use | CON-003, CON-007 |
| CC-015 | **Dual-factor confirmation** | Critical actions (wipe, debug enable) require conscious multi-step process | CON-006, CON-007 |

---

## 5. PRIVILEGED FUNCTION INVENTORY

### 5.1 Android Privilege Levels

| Privilege Level | Description | Functions Available | Access Method |
|---|---|---|---|
| **Standard User** | Normal app sandboxed execution | App operations, data access within sandbox | App launch |
| **Device Admin** | Enhanced device management | Lock, wipe, password policy | Settings > Security |
| **ADB Shell** | Android Debug Bridge | Package management, property access, logcat | USB + USB Debugging |
| **Root (NOT available)** | Superuser — full system access | Not available (bootloader locked, no root) | NOT AUTHORIZED |
| **TEE** | Trusted Execution Environment | Crypto operations, biometric matching | Hardware-isolated |
| **Bootloader** | Pre-boot firmware | Boot verification, flash operations | Locked — not accessible |
| **Recovery Mode** | Factory reset, cache wipe | Wipe data/cache, sideload (OEM signed only) | Power + Volume Up |

### 5.2 Privileged Operations Tracking

| Operation | Privilege Required | Frequency | Logging |
|---|---|---|---|
| Install application | Standard User | As needed | Play Store history |
| Change security settings | Standard User | Infrequent | Settings change logged |
| Enable USB Debugging | Standard User (Developer Options) | Assessment only | System log |
| Factory Reset | Standard User (PIN required) | Emergency only | Irreversible — must document before |
| ADB shell commands | ADB Shell | Assessment only | logcat |
| Modify system properties | Root (NOT available) | N/A — not possible | N/A |
| Unlock bootloader | Bootloader | N/A — PROHIBITED | N/A |

---

## 6. ACCESS CONTROL ENFORCEMENT

### 6.1 Role-Based Access Control (RBAC) Mapping

Since Android does not natively support role-based mode switching, logical role separation is enforced through:

| Role Context | How Activated | Controls Active | Restrictions |
|---|---|---|---|
| **End User Mode** (default) | Normal device unlock | Standard permissions | No Developer Options, no ADB |
| **Admin Mode** | Developer Options enabled | ADB access, system inspection | Must be temporary; documented |
| **Assessment Mode** | USB Debugging + ADB connected | Full ADB shell access | Physical supervision required; disable after |
| **Emergency Mode** | Recovery mode boot | Factory reset, cache wipe | Last resort only; backup first |

### 6.2 Planned Improvements (Work Profile)

When Android Work Profile is implemented (POA&M-MOD-003):

| Profile | Data Access | App Access | Admin Control |
|---|---|---|---|
| **Personal Profile** | Personal data only | Personal apps | User-managed |
| **Work Profile** | Federal data only | Approved Federal apps | MDM-managed (future) |

This provides additional logical separation between user and operational functions.

---

## 7. REVIEW AND AUDIT

| Activity | Frequency | Responsible |
|---|---|---|
| Review SoD Matrix | Annually | AAKASH (ISSM role) |
| Verify compensating controls effective | Semi-annually | AAKASH (Assessor role) |
| External audit of role conflicts | Annually | External Auditor (R-007) |
| Update for organizational changes | As needed | AAKASH |
| Privileged operation review | Quarterly | AAKASH (ISSM role) |

---

**Document Control**

| Version | Date | Author | Changes |
|---|---|---|---|
| 1.0 | March 9, 2026 | AAKASH | Initial Separation of Duties Matrix |

---

*END OF SEPARATION OF DUTIES MATRIX*
