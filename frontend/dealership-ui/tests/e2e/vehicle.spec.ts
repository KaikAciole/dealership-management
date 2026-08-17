import { test, expect, type Page } from '@playwright/test';

test.describe('Fluxo Crítico: Veículos', () => {
  
  test('Deve exibir erros do Zod e depois criar o veículo com sucesso', async ({ page }: { page: Page }) => {
    await page.goto('/vehicles/new');

    // Força o submit vazio para testar as constraints do Zod
    await page.getByRole('button', { name: /Cadastrar veiculo/i }).click();
    await expect(page.getByText('Marca deve ter ao menos 2 caracteres')).toBeVisible();
    await expect(page.getByText('Modelo e obrigatorio')).toBeVisible();

    // Preenche o caminho feliz
    await page.getByLabel('Marca').fill('Toyota');
    await page.getByLabel('Modelo').fill('Corolla');
    await page.getByLabel('Ano de fabricacao').fill('2024');
    
    // exact: true para não conflitar com "Cor externa"
    await page.getByLabel('Cor', { exact: true }).fill('Branco');
    await page.getByLabel('Cor externa').fill('Branco Perola');
    await page.getByLabel('Preco').fill('15000000'); 
    
    // Chassi dinâmico para não violar Unique Constraint no banco
    const uniqueChassis = '9BW' + Date.now().toString() + 'A';
    await page.getByLabel('Chassi').fill(uniqueChassis);
    
    // Seleciona a primeira concessionária do select
    await page.locator('select#dealershipId').selectOption({ index: 1 });

    // Testa o upload mockando o arquivo na memória
    await page.locator('input[type="file"]').setInputFiles({
      name: 'car.jpg',
      mimeType: 'image/jpeg',
      buffer: Buffer.from('conteudo-falso-de-imagem')
    });

    await page.getByRole('button', { name: /Cadastrar veiculo/i }).click();

    await expect(page.getByText('Veiculo cadastrado com sucesso')).toBeVisible();
    await expect(page).toHaveURL('/vehicles');
  });

  test('Deve listar e excluir um veículo atualizando o cache do React Query', async ({ page }: { page: Page }) => {
    await page.goto('/vehicles');

    const firstRow = page.locator('tbody tr').first();
    const vehicleName = await firstRow.locator('td').nth(1).innerText();

    // Clica no botão de lixeira usando aria-label
    await firstRow.getByRole('button', { name: /Remover Veículo/i }).click();

    // Clica no botão do AlertDialog do Shadcn
    await page.getByRole('button', { name: 'Sim, excluir' }).click();

    await expect(page.getByText('Veículo deletado com sucesso!')).toBeVisible();
    await expect(page.getByText(vehicleName)).not.toBeVisible();
  });
});