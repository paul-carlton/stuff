**Paul Carlton**: In GitHub Enterprise, the use of code owners, does that impact only pushes to main or does it work on PRs as well?

**ChatGPT**: In GitHub Enterprise, **CODEOWNERS** affects **pull requests (PRs)** as well as direct pushes to protected branches (such as `main`).

### How **CODEOWNERS** Works:
- When a pull request is created that modifies files specified in the **CODEOWNERS** file, the designated code owners are automatically **requested as reviewers**.
- If branch protection rules require **"Require review from Code Owners"**, then a PR **must** be approved by at least one code owner before it can be merged.
- Even if a user has **write access**, they may still need code owner approval before merging if this rule is enabled.
- If direct pushes are allowed, a **CODEOWNERS** file does not prevent them unless restricted by branch protection rules.

### Summary:
- **Affects PRs** by requiring approvals.
- **Affects pushes to main** if branch protection rules enforce code owner reviews.
- **Does not block merges or pushes unless enforced via branch protection rules.** 

Would you like details on setting up branch protection rules to enforce code owner reviews?

**Paul Carlton**: How do I restrict who can update files in a particular folder on GitHub?

**ChatGPT**: To restrict who can update files in a particular folder on GitHub, you can use a combination of **branch protection rules** and **CODEOWNERS** in GitHub Enterprise. Here’s how:

---

### **1. Use `CODEOWNERS` to Require Specific Approvers**
The `CODEOWNERS` file lets you define who must review changes to specific files or directories.

#### **Steps:**
1. **Create or update** a `.github/CODEOWNERS` file in your repository.
2. Add a rule specifying the folder and the required reviewers.

   Example:
   ```
   # Restrict changes to the `config/` folder
   config/ @team-security
   ```

   - This means any change in the `config/` directory requires approval from the `@team-security` team.

3. Commit the file to your repository.

---

### **2. Enforce Code Owner Reviews with Branch Protection**
To prevent unauthorized updates, enforce **code owner reviews** in branch protection settings.

#### **Steps:**
1. Go to your repository on GitHub.
2. Click on **Settings > Branches**.
3. Under **Branch protection rules**, select the branch you want to protect (e.g., `main`).
4. Enable:
   - **Require a pull request before merging**
   - **Require review from Code Owners**
   - Optionally, disable direct pushes to force all changes through PRs.

Now, only users in `@team-security` can approve changes to `config/` before merging.

---

### **3. Use Repository Rulesets (Optional)**
GitHub Enterprise also supports **rulesets** for more granular control. You can:
- Restrict who can push to specific files or directories.
- Require specific teams to approve changes.

#### **Steps:**
1. Go to **Repository Settings > Rulesets**.
2. Create a new ruleset.
3. Set:
   - **Target branch** (e.g., `main`).
   - **Protected file paths** (e.g., `config/**`).
   - **Required reviewers** (e.g., `@team-security`).
   - **Restrict who can push changes** to this folder.

---

### **Final Result:**
- Only designated users or teams can approve changes to restricted folders.
- Unauthorized users cannot push changes directly or merge PRs without approval.

Would you like guidance on setting up **rulesets** for more advanced restrictions?

