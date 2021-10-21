# Generated by Django 3.2.8 on 2021-10-21 19:40

from django.db import migrations, models
import django.db.models.deletion


class Migration(migrations.Migration):

    dependencies = [
        ('maize_api', '0013_data_crop_type'),
    ]

    operations = [
        migrations.CreateModel(
            name='User',
            fields=[
                ('user_name', models.CharField(max_length=100, primary_key=True, serialize=False)),
                ('password', models.CharField(blank=True, max_length=150)),
                ('email', models.CharField(max_length=100)),
            ],
        ),
        migrations.AlterField(
            model_name='data',
            name='user_id',
            field=models.ForeignKey(on_delete=django.db.models.deletion.CASCADE, to='maize_api.user'),
        ),
    ]
