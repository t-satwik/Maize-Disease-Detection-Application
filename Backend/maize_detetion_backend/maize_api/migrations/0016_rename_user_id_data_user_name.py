# Generated by Django 3.2.8 on 2021-10-22 06:37

from django.db import migrations


class Migration(migrations.Migration):

    dependencies = [
        ('maize_api', '0015_auto_20211022_0122'),
    ]

    operations = [
        migrations.RenameField(
            model_name='data',
            old_name='user_id',
            new_name='user_name',
        ),
    ]