# CONFIGURATION MANAGEMENT PLAN

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

## 1. INTRODUCTION

This Configuration Management Plan (CMP) establishes the processes for managing and controlling changes to the VTMIS, maintaining baseline configurations, and ensuring configuration integrity throughout the system lifecycle. This plan is developed in accordance with NIST SP 800-128 and FedRAMP CM requirements.

---

## 2. CONFIGURATION MANAGEMENT ROLES

| Role | Name | Responsibility |
|---|---|---|
| Configuration Manager | AAKASH | Overall CM process management |
| Change Authority | AAKASH | Approve/deny change requests |
| System Administrator | AAKASH | Implement approved changes |

---

## 3. BASELINE CONFIGURATION

### 3.1 Hardware Baseline (as of March 9, 2026)

| Component | Baseline Value |
|---|---|
| Device Model | Vivo T2X 5G (V2312 / V2225) |
| Serial Number | 10BDC81HTA000Z4 |
| Processor | MediaTek Dimensity 6020 (MT6833), 8-core, 2.2 GHz |
| RAM | 6 GB Physical (5,755,288 kB usable) |
| Storage | 128 GB internal (106 GB user partition) |
| SIM | Jio (India, LTE/5G NR) |
| Display | 1080x2408, 440 PPI |
| Battery | 5000 mAh Li-Polymer |

### 3.2 Software Baseline (as of March 9, 2026)

| Component | Baseline Value |
|---|---|
| Android Version | 15 (API Level 35) |
| Custom OS | FuntouchOS 15 |
| Build Number | PD2230KF_EX_A_15.2.13.1.W30 |
| Build Fingerprint | vivo/V2225C/V2225:15/AP3A.240905.015.A2_MOD1/compiler250926152015:user/release-keys |
| Kernel | Linux 4.19.236+ (SMP PREEMPT, compiled Sep 26, 2025) |
| Security Patch Level | 2025-05-01 |
| Baseband | MOLY.NR15.R3.TC19.PR4.SP.V1.P191 |
| Total Packages | 308 (38 user-installed, 270 system) |
| Build Type | user (production) |
| Build Incremental | compiler250926152015 |

### 3.3 Security Configuration Baseline

| Setting | Required Value | Current Value | Compliant |
|---|---|---|---|
| Encryption | Enabled (FBE) | encrypted/file | ✅ |
| SELinux | Enforcing | Enforcing | ✅ |
| Verified Boot | Green | green | ✅ |
| Bootloader | Locked | locked (flash.locked=1) | ✅ |
| Screen Lock | PIN minimum | PIN | ✅ |
| USB Debugging | Disabled | **Enabled** | ❌ |
| Developer Options | Disabled | **Enabled** | ❌ |
| OEM Unlock | Disabled | Disabled | ✅ |
| Accessibility Services | None | null (none) | ✅ |
| Device Admin | MDM enrolled | **None** | ❌ |
| VPN | Always-on | **Not configured** | ❌ |

---

## 4. CHANGE MANAGEMENT PROCESS

### 4.1 Change Categories

| Category | Description | Approval Required | Examples |
|---|---|---|---|
| **Standard** | Pre-approved routine changes | No (pre-approved) | Security patch application, Play Store app updates |
| **Normal** | Planned changes requiring review | Yes (Change Authority) | New app installation, OS upgrade, config change |
| **Emergency** | Urgent changes to address security threat | Post-implementation review | Vulnerability mitigation, incident response |

### 4.2 Change Request Process

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   Request     │────▶│   Review &   │────▶│   Implement  │
│   Change      │     │   Approve    │     │   Change     │
└──────────────┘     └──────────────┘     └──────┬───────┘
                                                  │
┌──────────────┐     ┌──────────────┐     ┌──────▼───────┐
│   Close       │◀────│   Update     │◀────│   Verify &   │
│   Request     │     │   Docs       │     │   Test       │
└──────────────┘     └──────────────┘     └──────────────┘
```

### 4.3 Change Request Template

```
CHANGE REQUEST
===============
CR ID:              [VTMIS-CR-YYYY-NNN]
Date:               [YYYY-MM-DD]
Requested By:       [Name]
Category:           [Standard/Normal/Emergency]

DESCRIPTION:
[What is being changed]

JUSTIFICATION:
[Why the change is needed]

IMPACT ANALYSIS:
- Security Impact:     [None/Low/Moderate/High]
- Availability Impact: [None/Low/Moderate/High]
- Rollback Plan:       [How to revert if needed]

IMPLEMENTATION STEPS:
1. [Step 1]
2. [Step 2]
3. [Verification step]

APPROVAL:
- Change Authority:  [Approved/Denied] - [Date]
- Implementation:    [Completed] - [Date]
- Verification:      [Passed/Failed] - [Date]
```

---

## 5. CONFIGURATION MONITORING

### 5.1 Automated Configuration Checks

The following ADB commands verify security configuration compliance:

```bash
# Encryption status
adb shell getprop ro.crypto.state          # Expected: encrypted

# SELinux status  
adb shell getenforce                        # Expected: Enforcing

# Verified boot
adb shell getprop ro.boot.verifiedbootstate # Expected: green

# Bootloader lock
adb shell getprop ro.boot.flash.locked      # Expected: 1

# Security patch level
adb shell getprop ro.build.version.security_patch

# USB debugging status
adb shell getprop sys.usb.state             # Expected: mtp (no 'adb')

# Package count (detect unauthorized installs)
adb shell pm list packages | wc -l          # Baseline: 308

# User app count
adb shell pm list packages -3 | wc -l      # Baseline: 38
```

### 5.2 Configuration Drift Detection

| Check | Frequency | Baseline | Alert Condition |
|---|---|---|---|
| Package count | Weekly | 308 total / 38 user | Any change |
| Security patch | Weekly | 2025-05-01 | Should only increase |
| SELinux status | Monthly | Enforcing | Any change from Enforcing |
| Boot state | Per boot | green | Any non-green state |
| Encryption | Monthly | encrypted | Any change |
| Build fingerprint | Monthly | vivo/V2225C/V2225:15/... | Unauthorized change |

---

## 6. SOFTWARE MANAGEMENT

### 6.1 Application Installation Policy

| Rule | Description |
|---|---|
| **Source** | Only from Google Play Store (no sideloading) |
| **Approval** | New apps require change request and security review |
| **Permissions** | Review and minimize permissions before installation |
| **Updates** | Auto-update enabled for security patches |
| **Removal** | Unused apps must be uninstalled within 30 days |

### 6.2 Current User-Installed Applications (Baseline)

| Package Name | Application | Category | Risk Notes |
|---|---|---|---|
| com.adobe.scan.android | Adobe Scan | Productivity | Cloud processing |
| com.aratai.chat | Aratai Chat | Communication | Review data handling |
| com.clusterdev.tamilkeyboard | Tamil Keyboard | Input | Keystroke access |
| com.digilocker.android | DigiLocker | Government ID | PII storage |
| com.facebook.katana | Facebook | Social Media | Data collection risk |
| com.google.android.apps.bard | Google Gemini | AI/LLM | Cloud processing |
| com.google.android.apps.docs | Google Drive | Cloud Storage | Data sync |
| com.google.android.apps.docs.editors.docs | Google Docs | Productivity | Cloud processing |
| com.google.android.apps.tachyon | Google Meet/Duo | Communication | — |
| com.instagram.android | Instagram | Social Media | Data collection risk |
| com.iob.iobconnect | IOB Connect | Banking | Financial credentials |
| com.jio.myjio | MyJio | Carrier | Network management |
| com.letterboxd.letterboxd | Letterboxd | Entertainment | Low risk |
| com.linkedin.android | LinkedIn | Professional | Data collection |
| com.mxtech.videoplayer.ad | MX Player | Media | Ad-supported |
| com.netflix.mediaclient | Netflix | Entertainment | Low risk |
| com.openai.chatgpt | ChatGPT | AI/LLM | Cloud processing |
| com.phonepe.app | PhonePe | Payment | Financial credentials |
| com.picsart.studio | Picsart | Media | Cloud processing |
| com.spotify.music | Spotify | Music | Low risk |
| com.twitter.android | X (Twitter) | Social Media | Data collection |
| com.whatsapp | WhatsApp | Messaging | E2E encrypted |
| com.wonder | Wonder | AI/Creative | Cloud processing |
| com.zoho.accounts.oneauth | Zoho OneAuth | Authentication | MFA tokens |
| com.zoho.creator.a | Zoho Creator | Low-code | Cloud sync |
| com.zoho.people | Zoho People | HR | PII handling |
| cris.org.in.prs.ima | IRCTC/PRS | Travel | PII (booking data) |
| in.startv.hotstar | Disney+ Hotstar | Entertainment | Low risk |
| naukriApp.appModules.login | Naukri | Job Search | PII (resume) |
| net.one97.paytm | Paytm | Payment | Financial credentials |
| org.sadhguru.miracleofmind | Sadhguru | Wellness | Low risk |
| org.telegram.messenger | Telegram | Messaging | E2E available |
| org.videolan.vlc | VLC | Media | Local playback, low risk |
| sharefiles.sharemusic.shareapps.filetransfer | File Transfer | Utility | Unencrypted transfer risk |
| stepcounter.pedometer.stepstracker | Step Counter | Health | Location patterns |

---

## 7. PATCH MANAGEMENT

### 7.1 Patch Sources

| Source | Type | Delivery |
|---|---|---|
| Vivo OTA | OS + Security Patches | Automatic/Manual (Settings > Software Update) |
| Google Play Store | App Updates | Automatic |
| Google Play System Updates | Modular security updates | Automatic (via Play Store) |

### 7.2 Patch Application Procedure

1. **Notification:** Check for available updates weekly
2. **Review:** Read patch notes/security bulletin
3. **Backup:** Verify recent backup exists
4. **Install:** Apply update (preferably connected to power and Wi-Fi)
5. **Verify:** Confirm new security patch level via `getprop ro.build.version.security_patch`
6. **Document:** Update baseline configuration and SSP

---

**Document Control**

| Version | Date | Author | Changes |
|---|---|---|---|
| 1.0 | March 9, 2026 | AAKASH | Initial Configuration Management Plan |

---

*END OF CONFIGURATION MANAGEMENT PLAN*
