import { test, expect } from '@playwright/test';

test.describe('Task Management', () => {
  test.beforeEach(async ({ page }) => {
    // Navigate to the task manager page before each test
    await page.goto('/task-manager/tasks');
  });

  // test to check if the task manager page is loaded
  test('should load the task manager page', async ({ page }) => {
    await expect(page).toHaveURL('/task-manager/tasks');

    // check if the page title is Task Manager
    await expect(page.locator('h1')).toHaveText('Task Manager');

    // check if the page has a anchor tag with text "All"
    await expect(page.locator('a:has-text("All")')).toBeVisible();

    // check if the page has a anchor tag with text "Completed"
    await expect(page.locator('a:has-text("Completed")')).toBeVisible();
    await expect(page.locator('a:has-text("Active")')).toBeVisible();
    
    // check if the page has a anchor tag with text "Add New Task"
    await expect(page.locator('a:has-text("Add New Task")')).toBeVisible();
  });

  test('should create a new task', async ({ page }) => {
    // Generate a unique task title using timestamp
    const uniqueTaskTitle = `Test Task ${Date.now()}`;
    
    // Click the "New Task" anchor tag
    await page.click('a:has-text("Add New Task")');
    
    // Fill in the task details
    await page.fill('input[name="title"]', uniqueTaskTitle);
    await page.fill('textarea[name="description"]', 'This is a test task');
    // from the dropdown select the priority as high
    await page.selectOption('select[name="priority"]', 'High Priority');
    
    // Submit the form
    await page.click('button:has-text("Add Task")');
    
    await expect(page).toHaveURL('/task-manager/tasks');
    
    // Find the specific row containing our unique task
    const taskRow = page.locator('tr', { has: page.locator(`td:has-text("${uniqueTaskTitle}")`) });
    
    // Verify the task was created with correct details
    await expect(taskRow).toBeVisible();
    await expect(taskRow.locator('td').nth(0)).toHaveText(uniqueTaskTitle);
    await expect(taskRow.locator('td').nth(1)).toHaveText('This is a test task');
  });


}); 